#include "suggestion.h"

void Suggestion::Process()
{
    // initilize the statistics
    m_total_number = 0;
    m_mrr = 0.0;
    m_top1_correct = 0;
    m_top5_correct = 0;
    m_top10_correct = 0;
    m_total_prob = 0.0;
    
    if (Data::DEBUG)
    {
        m_fout.open(Data::OUTPUT_FILE.c_str());
    }

    if (Data::FILES)
    {
        DealFiles();
    }
    else
    {
        DealFile(Data::INPUT_FILE);
    }

    cout << "Total tokens: " << m_total_number << endl;
    if (Data::ENTROPY)
    {
        cout << "Entropy: " << -m_total_prob/m_total_number << endl;
    }
    else
    {
        cout << "Mean reciprocal rank: " << m_mrr/m_total_number << endl;
        cout << "Total top1 accuracy: " << (float)m_top1_correct/m_total_number << endl;
        cout << "Total top5 accuracy: " << (float)m_top5_correct/m_total_number << endl;
        cout << "Total top10 accuracy: " << (float)m_top10_correct/m_total_number << endl;
    }
}

void Suggestion::DealFiles()
{
    vector<string> input_files;
    ifstream fin(Data::INPUT_FILE.c_str());
    string line;
    while (getline(fin, line))
    {
        input_files.push_back(line);
    }
    fin.close();

    ifstream fin_list(Data::INPUT_FILE.c_str());

    ofstream fout_file_measures((Data::INPUT_FILE+".file.measures").c_str());

    ifstream fin_scope_list;
    if (Data::USE_RELATED_FILE)
    {
        fin_scope_list.open(Data::SCOPE_FILE.c_str());
    }

    string file_name;
    string scope_file_name;
    while (getline(fin_list, file_name))
    {
        ifstream fin(file_name.c_str());
        getline(fin_scope_list, scope_file_name);
        ReadScope(scope_file_name);

        if (Data::USE_RELATED_FILE)
        {
            for (int i=0; i<(int)m_related_files.size(); ++i)
            {
                Data::CACHE.Build(Data::FILE_DIR + "/" + m_related_files.at(i), Data::CACHE_ORDER);
                if (Data::DEBUG)
                {
                    cout << "related file: " << Data::FILE_DIR + "/" + m_related_files.at(i) << endl;
                    cout << "cache: " << Data::CACHE.m_records.size() << endl;
                }
            }
        }

        DealFile(file_name);

        if (Data::USE_FILE_CACHE)
        {
            Data::CACHE.Clear();
        }

        if (Data::ENTROPY)
        {
           fout_file_measures << -m_file_prob/m_file_total_number << endl;
        }
        else
        {
            fout_file_measures << m_file_mrr/m_file_total_number << " ||| "
                               << (float)m_file_top1_correct/m_file_total_number << " ||| "
                              << (float)m_file_top5_correct/m_file_total_number << " ||| "
                               << (float)m_file_top10_correct/m_file_total_number << endl;
        }
    }

}

void Suggestion::DealFile(const string& input_file)
{
    // initilize the statistics for each file
    m_file_total_number = 0;
    m_file_mrr = 0.0;
    m_file_top1_correct = 0;
    m_file_top5_correct = 0;
    m_file_top10_correct = 0;
    m_file_prob = 0.0;

    // get the tokens of the input file
    vector<string> tokens;
    tokens.push_back("<s>");
    
    ifstream fin(input_file.c_str());
    string line, token;
    while (getline(fin, line))
    {
        stringstream ss(line);

        while (ss >> token)
        {
            tokens.push_back(token);
        }
    }
    

    // analysis the tokens
    // common usage
    string prefix, cache_prefix;
    string debug_info;
    int start;

    // for predicting task
    vector<Word> candidates;
    int rank;

    // for calculating the cross entropy
    float prob;

    for (int i=1; i<(int)tokens.size(); i++)
    {
        start = i-(Data::NGRAM_ORDER-1)>0? i-(Data::NGRAM_ORDER-1) : 0;
        Join(tokens, start, i-1, prefix);
        
        if (Data::USE_CACHE)
        {
            // generate new prefix for cache
            start = i-(Data::CACHE_ORDER-1)>0 ? i-(Data::CACHE_ORDER-1) : 0;
            Join(tokens, start, i-1, cache_prefix);
        }

        if (Data::ENTROPY)
        {
            prob = GetProbability(prefix, cache_prefix, tokens.at(i), debug_info);
            m_file_prob += prob;

            if (Data::DEBUG)
            {
                 m_fout << "<bead>" << endl;
                 m_fout << "<ref> " << tokens.at(i) << " </ref>" << endl;
                 m_fout << "<prob> " << prob << " ||| " << debug_info << " </prob>" << endl;
                 m_fout << "</bead>" << endl;
            }
        }
        else
        {

            GetCandidates(prefix, cache_prefix, candidates);
            // for calcuting accuracy
            rank= GetRank(candidates, tokens.at(i));
            if (rank > 0)
            {
                m_file_mrr += 1/(float)rank;
            }
            m_file_top1_correct += Is_in(candidates, tokens.at(i), 1);
            m_file_top5_correct += Is_in(candidates, tokens.at(i), 5);
            m_file_top10_correct += Is_in(candidates, tokens.at(i), 10);
      
            if (Data::DEBUG)
            {
                m_fout << "<bead>" << endl;
                m_fout << "<ref>" << tokens.at(i) << "</ref>" << endl;
                for (int j=0; j<(int)candidates.size(); j++)
                {
                    if (j == 10)
                    {
                        break;
                    }

                    m_fout << "<cand id=" << j+1 << "> " 
                           << candidates.at(j).m_token << " ||| " << candidates.at(j).m_prob 
                           << " ||| " << candidates.at(j).m_debug
                           << " </cand>" << endl;
                }
                m_fout << "</bead>" << endl;
            }
        }
         
        // update the cache
        if (Data::USE_CACHE)
        {
            if (Data::USE_WINDOW_CACHE)
            {
                Data::CACHE.Update(cache_prefix, tokens.at(i), Data::WINDOW_SIZE);
            }
            else
            {
                Data::CACHE.Update(cache_prefix, tokens.at(i));
            }
        }
    }
    
    int num = (int)tokens.size() - 1;
    m_file_total_number = num;
    m_total_number += m_file_total_number;
    
    if (Data::ENTROPY)
    {
        // calcuting the cross entropy
        m_total_prob += m_file_prob;
    }
    else
    {
        // calcuting accuracy
        m_mrr += m_file_mrr;
        m_top1_correct += m_file_top1_correct;
        m_top5_correct += m_file_top5_correct;
        m_top10_correct += m_file_top10_correct;
    }
}



void Suggestion::GetCandidates(const string& prefix,
                               const string& cache_prefix,
                               vector<Word>& candidates)
{
    if (!Data::USE_CACHE_ONLY)
    {
        // n-gram word candidates
        Data::NGRAM->GetCandidates(prefix, Data::USE_BACKOFF, candidates);
        if (Data::DEBUG)
        {
            for (int i=0; i<(int)candidates.size(); ++i)
            {
                candidates.at(i).m_debug = "ngram prob: " + to_string(candidates.at(i).m_prob);
            }
        }
    }
    else
    {
        candidates.clear();
    }

    if (Data::USE_CACHE)
    {
        // update the candidates according to the cache
        Data::CACHE.UpdateCandidates(cache_prefix, Data::CACHE_LAMBDA, Data::CACHE_DYNAMIC_LAMBDA, candidates);
    }
}


float Suggestion::GetProbability(const string& prefix, 
                                 const string& cache_prefix, 
                                 const string& token, 
                                 string& debug_info)
{
    float prob = 0.0;
    debug_info.clear();

    if (!Data::USE_CACHE_ONLY)
    {
        prob = Data::NGRAM->GetProbability(prefix, token, Data::USE_BACKOFF);
        if (Data::DEBUG)
        {
            debug_info = "ngram prob: " + to_string(pow(2, prob));
        }
    }

    if (Data::USE_CACHE)
    {
        int cache_count = Data::CACHE.GetCount(cache_prefix);
        if (cache_count != 0)
        {
            float cache_discount = Data::CACHE_LAMBDA;
            if (Data::CACHE_DYNAMIC_LAMBDA)
            {
                //P(cnd) = 1/(cache_count+1)*P(lm) + cache_count/(cache_count+1)*P(cache)
                cache_discount = (float)cache_count/(cache_count+1);
            }

            float ngram_discount = 1-cache_discount;

            int ngram_count = Data::CACHE.GetCount(cache_prefix, token);
            prob = ngram_discount * pow(2, prob) + cache_discount * ((float)ngram_count/cache_count);
            debug_info += ", in cache: " + to_string(ngram_count) + "/" + to_string(cache_count);
            
            if (prob > 0.0)
            {
                prob = log2(prob);
            }
            else
            {
                prob = Data::NGRAM->m_unk_prob;
            }
        }
    }

    return prob;
}


int Suggestion::GetRank(const vector<Word>& candidates, const string& ref)
{
    for (int i=0; i<(int)candidates.size(); i++)
    {
        if (candidates.at(i).m_token == ref)
        {
            return i+1;
        }
    }

    return 0;
}

int Suggestion::Is_in(const vector<Word>& candidates, const string& ref, const int top_n)
{
    int end = top_n < candidates.size() ? top_n : candidates.size();
    for (int i=0; i<end; i++)
    {
        if (candidates.at(i).m_token == ref)
        {
            return 1;
        }
    }

    return 0;
}

void Suggestion::ReadScope(const string& scope_file_name)
{
    m_class_scope_begins.clear();
    m_class_scope_ends.clear();
    m_method_scope_begins.clear();
    m_method_scope_ends.clear();
    m_related_files.clear();

    ifstream fin(scope_file_name.c_str());
    string line;

    vector<string> items;

    while (getline(fin, line))
    {
        if (startswith(line, "<class>"))
        {
            while (getline(fin, line))
            {
                if (startswith(line, "</class>"))
                {
                    break;
                }

                Split(line, items);
                m_class_scope_begins.insert(atoi(items.at(0).c_str()));
                m_class_scope_ends.insert(atoi(items.at(1).c_str()));
            }
        }
        if (startswith(line, "<method>"))
        {
            while (getline(fin, line))
            {
                if (startswith(line, "</method>"))
                {
                    break;
                }

                Split(line, items);
                m_method_scope_begins.insert(atoi(items.at(0).c_str()));
                m_method_scope_ends.insert(atoi(items.at(1).c_str()));
            }
        }
        if (startswith(line, "<file>"))
        {
            while (getline(fin, line))
            {
                if (startswith(line, "</file>"))
                {
                    break;
                }

                m_related_files.push_back(line);
            }
        }
    }
}

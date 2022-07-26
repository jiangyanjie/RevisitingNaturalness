### We compute the entropy for each of the code lines in the same way as Ray et al.

We reuse this tool to generate entropy of each code line. The author's instructions are very straightforward, so I'll  **reuse** them here. You can download from https://bitbucket.org/tuzhaopeng/cachelm_for_code_suggestion/src/master/

### 1. Preprocess

You should extract all java files from the project, and do necessary preprocess: remvoe comments, tokenization. Go to the annotation folder, and run './walk.py':

`./walk.py input_dir output_dir`

### 2. Evaluation

After preprocessing, you should rename the output folder to "files" and put them in the "evaluation" folder. 

For example, mkdir a folder named "data" in "evaluation", and the structure is:

      | evaluation
          | data
               | sample_project
                   | files    // this contains all processed files obtained from Step1



#### 2.1 Training

`./bin/train.py data/sample_project 3`

Here "3" denotes the order of n-grams trained from the "sample_project". Here we use 3-grams.

Possible Troubles:
1. If you find the LM tools 'ngram' and 'ngram_count' cannot work on your machine. Try to download the latest version from the website 'http://www.speech.sri.com/projects/srilm/download.html', and compile it on your own machine. Replace the original ones with your compiled procedures.


#### 2.1 Testing

Try to find the full command list by typing "./completion".

    the necessary parameters: 
    ---------------------------------------------------------------
    -INPUT_FILE         the input file
    -NGRAM_FILE         the ngrams file
    -NGRAM_ORDER        the value of N (order of lm)
    ---------------------------------------------------------------

    the optional parameters:
    ---------------------------------------------------------------
    -ENTROPY            calculate the cross entropy of the test file
                        rather than providing the suggestions
    -TEST               test mode, no output, no debug information
    -FILES              test on files or not, default on a single file
    -DEBUG              output debug information
    -OUTPUT_FILE        the output file
    ---------------------------------------------------------------
    -BACKOFF            use the back-off technique
    -CACHE              use the cache technique 
    -CACHE_ONLY         only use the cache technique without ngrams
    -CACHE_ORDER        the maximum order of ngrams used in the cache (default: 3)
    -CACHE_DYNAMIC_LAMBDA   dynamic interpolation weight for -CACHE (H/(H+1)), default option
    -CACHE_LAMBDA       interpolation weight for -CACHE
    -WINDOW_CACHE       build the cache on a window of n tokens (default n=1000)
    -WINDOW_SIZE        the size of cache, default: 1000 tokens
    -FILE_CACHE         build the cache on a file or related files
    -SCOPE_FILE         the scope file for scope cache on CLASS or METHOD
    -RELATED_FILE       when using cache on file scope, build the cache on the related files
                        FILE_DIR should be given
    -FILE_DIR           the directory that stores all files
    ---------------------------------------------------------------


Try to find examples from "entropy.bat" and "suggestion.bat" for calculating entropies and code suggestion, respectively. 




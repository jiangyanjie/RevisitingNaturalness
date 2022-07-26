import java.io.File;
import java.util.ArrayList;

public class getOriginalLineEntropy {

    public static  ArrayList<String> getLineToken(String projectName, int bugid, String version, int fileid){
        ArrayList<String> tokens = new ArrayList<>();

        String path = "./afterGenerationEntropy/"+projectName+"/"+projectName+"_"+bugid+"_"+version+"_entropy/"+fileid+"/";
        File folder = new File(path);
        File[] fileList = folder.listFiles();


        for(File file : fileList){
            String fileName = file.getName();
            if(fileName.endsWith(".log")){
                String filePath = file.getAbsolutePath();
                ArrayList<String> context = getFileNO.readFile(filePath);
                for(String each : context){
                    if(each.startsWith("Token is(ttt) :")){
                        String eachtoken = each.split("(ttt) :")[1];
                        tokens.add(eachtoken);
                    }
                }
            }
        }
        return tokens;
    }

    public static ArrayList<String> getLineEntropy(String projectName, int bugid, String version, int fileid){
        ArrayList<String> entropy = new ArrayList<>();
        String path = "./afterGenerationEntropy/"+projectName+"/"+projectName+"_"+bugid+"_"+version+"_entropy/"+fileid+"/";
        File folder = new File(path);
        File[] fileList = folder.listFiles();


        for(File file : fileList){
            String fileName = file.getName();
            if(fileName.endsWith(".log")){
                String filePath = file.getAbsolutePath();
                ArrayList<String> context = getFileNO.readFile(filePath);
                for(String each : context){
                    if(each.startsWith("prob(jyj) :")){
                        String eachprob = each.split(": ")[1];
                        entropy.add(eachprob);
                    }
                }
            }
        }
        return entropy;
    }

    public static ArrayList<String> lineCode(String projectName, int bugid, String version, int fileid){
        String path = "./afterGenerationEntropy/"+projectName+"/output_"+projectName+"_"+bugid+"_"+version+"/"+fileid+".code.java.lines";

        return getFileNO.readFile(path);
    }



    public static void calculateEntropy(ArrayList<String> entropys, ArrayList<String> lineCode,String projectName, int bugid, String version, int fileid){
        File fff = new File("./afterGenerationEntropy/"+projectName+"/"+projectName+"_"+bugid+"_"+version+"_entropy/"+fileid+"/entropy.txt");
        if(fff.exists()){
            fff.delete();
        }

        int pointer=0;
             for(int i=0;i<lineCode.size();i++){
                 double sum=0.0;
                 double average=0.0;
                 String[] eachLineToken = lineCode.get(i).trim().split(" ");
                 int len = eachLineToken.length;

                 for(int k=pointer; k<len+pointer; k++){
                     if(k >= entropys.size()){
                         return;
                     }
                     try{
                         double entropy = Double.parseDouble(entropys.get(k));
                         sum += entropy;
                     }catch(Exception e){

                     }

                 }
                 pointer +=len;
                 average = sum/len;
                 getFileNO.appendFile((i+1)+","+average+"\n","./afterGenerationEntropy/"+projectName+"/"+projectName+"_"+bugid+"_"+version+"_entropy/"+fileid+"/entropy.txt");
//                 System.out.println((i+1)+","+average);
             }
    }

    public static int getFileNumer(String projectName, int bugid, String version){
        String path="./afterGenerationEntropy/"+projectName+"/"+projectName+"_"+bugid+"_"+version+"_entropy/";
        int num=0;
        File folder = new File(path);
        File list[] = folder.listFiles();
        for(int i = 0; i < list.length; i++){
            if(list[i].isDirectory()){
                num++;
            }
        }
        System.out.println(num);
        return num;
    }



    public static void main(String[] args){
        System.out.println("----start----");
        String projectName=setting.projectName;//"Ognl";
        int[] bugids = setting.bugids;//new int[]{1};
        String[] versions = new String[]{"free","buggy","fix"};
        for(int k=0;k<bugids.length;k++){
            int bugid = bugids[k];
            for(int tt=0;tt<versions.length;tt++){
                String version = versions[tt];

                int num = getFileNumer(projectName,bugid,version);

                for(int i=0;i<num;i++){
                    ArrayList<String> t = lineCode(projectName,bugid,version,i);
                    System.out.println(t.size());
                    ArrayList<String> entropys = getLineEntropy(projectName,bugid,version,i);

                    calculateEntropy(entropys,t,projectName,bugid,version,i);

                    System.out.println("finished " +i + version +"::" + bugid);
                }
            }

        }
    }

}

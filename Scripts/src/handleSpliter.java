import java.util.ArrayList;

public class handleSpliter {

    public static void main(String[] args){
        System.out.println("------start------");

        for(int k=0;k<setting.bugids.length;k++){
            int bugid = setting.bugids[k];
            String[] versions = new String[]{"free","buggy","fix"};
            for(int i=0;i< versions.length;i++){
                String version = versions[i];
                handleSplit(setting.projectName,bugid,version);
            }
        }
    }

    public static void handleSplit(String projectName,int bugid,String version){
        String path = "./"+projectName+"/output_"+projectName+"_"+bugid+"_"+version+"/fileinfo.txt";
        ArrayList<String> ctx = getFileNO.readFile(path);
        for(int i=0;i<ctx.size();i++){
            String[] eachLine = ctx.get(i).split("\t");
            getFileNO.appendFile(eachLine[0] + "," + eachLine[1]+"\n","./"+projectName+"/output_"+projectName+"_"+bugid+"_"+version+"/fileinfo1.txt");
        }
    }
}

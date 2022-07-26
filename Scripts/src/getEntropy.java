import java.io.File;
import java.util.*;

public class getEntropy {

    public static ArrayList<String> getBuggyEntropy(String projectName, String bugid, String version){
        String path = "./"+projectName+"/"+ projectName+"_"+bugid+"_"+version+"_entropy";
        File floder = new File(path);
        File[] files = floder.listFiles();
        ArrayList<String> paths = new ArrayList<>();
        for(File file: files){
            if(file.isDirectory()){
                paths.add(file.getPath());
            }
        }
        Collections.sort(paths);
//        for(String t: paths){
//            System.out.println(t);
//        }
        return paths;
    }

    public static ArrayList<String> getChangeLine(String projectName,String bugid, String version){
        ArrayList<String> changes = getFileNO.readFile("./AChangeLine/v23.txt");
        ArrayList<String> res = new ArrayList<>();
        for(String str: changes){
            String[] strs = str.split(",");
            String pn = strs[0];
            String bi = strs[1];
            String ver = strs[2];
            if(projectName.equals(pn) && bugid.equals(bi) && version.equals(ver)){
//                System.out.println(str);
                String fileid = strs[3];
                int pos1= str.indexOf("[");
                int pos2 = str.indexOf("]");
                String lines = str.substring(pos1+1,pos2);
                if(!lines.equals("")){
                    res.add(fileid+"@@"+lines);
                }
            }
        }
        return res;
    }

    public static void getV23Free2Buggy(String projectName, String bugid,String version){
        ArrayList<String> paths = getBuggyEntropy(projectName,bugid,version);
        for(String s : paths){
            int pos1 = s.lastIndexOf("/");
            String fileid = s.substring(pos1+1);
//            System.out.println(s);
            String newLineEntropy = s+"/newLineEntropy.txt";
            ArrayList<String> newEntroy = getFileNO.readFile(newLineEntropy);
            ArrayList<String> changeLine = getChangeLine(projectName,bugid,version);
//            System.out.println(changeLine);
            for(String ne : newEntroy){
                String[] lines = ne.split(",");
                String lineid = lines[0];
                String entropy = lines[3];
                if(changeLine.size() == 0){
//                    System.out.println("buggy free: " + entropy);
                    getFileNO.appendFile(entropy+"\n","./AChangeLine/entropyV23buggyfreeLines.txt");
                }else{
                    for(String cl : changeLine){
                        String[] clines = cl.split("@@");

                        String fileid2 = clines[0];
                        ArrayList<String> lines2 = change2Array(clines[1]);
//                        System.out.println(lines2.size());
                        if(fileid.equals(fileid2)){
//                            for(String l2: lines2){
                            for(int t=0;t<lines2.size();t++){
                                String l2 = lines2.get(t);
//                                System.err.println(l2);
                                if(l2.equals(lineid)){
                                    System.out.println("buggy lines: " + entropy);
                                    getFileNO.appendFile(entropy+"\n","./AChangeLine/entropyV23buggyLines.txt");
                                }else{
//                                    System.out.println("buggy-free lines: " + entropy);
                                    getFileNO.appendFile(entropy+"\n","./AChangeLine/entropyV23buggyfreeLines.txt");
                                }
                            }
                        }else{
//                            System.out.println("buggy free: " + entropy);
                            getFileNO.appendFile(entropy+"\n","./AChangeLine/entropyV23buggyfreeLines.txt");
                        }
                    }
                }

            }
//            System.out.println(newLineEntropy);
        }
    }

    public static void getV23fix(String projectName, String bugid,String version){
        ArrayList<String> paths = getBuggyEntropy(projectName,bugid,version);
        for(String s : paths){
            int pos1 = s.lastIndexOf("/");
            String fileid = s.substring(pos1+1);
            String newLineEntropy = s+"/newLineEntropy.txt";
            ArrayList<String> newEntroy = getFileNO.readFile(newLineEntropy);
            ArrayList<String> changeLine = getChangeLine(projectName,bugid,version);
            for(String ne : newEntroy){
                String[] lines = ne.split(",");
                String lineid = lines[0];
                String entropy = lines[3];
                if(changeLine.size() !=0){
                    for(String cl : changeLine){
                        String[] clines = cl.split("@@");
                        String fileid2 = clines[0];
                        ArrayList<String> lines2 = change2Array(clines[1]);
                        if(fileid.equals(fileid2)){
                            for(String l2: lines2){
                                if(lineid.equals(l2)){
//                                    System.out.println("fix lines: " + entropy);
                                    getFileNO.appendFile(entropy+"\n","./AChangeLine/entropyV23fixLines.txt");
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public static ArrayList<String> change2Array(String line){
        String[] lines = line.split(",");
        ArrayList<String> res = new ArrayList<>();
        for(int i=0;i<lines.length;i++){
            res.add(lines[i].trim());
        }
        return res;
    }

    public static void getEntropy1(){
        System.out.println("----------------start----------------");

        String projectName = setting.projectName;//"Dbutils";
        int[] bugids = setting.bugids;// new int[]{1,2};


        for(int i=0;i< bugids.length;i++){
            String bugid = bugids[i]+"";
            getV23Free2Buggy(projectName,bugid,"buggy");
            getV23fix(projectName,bugid,"fix");
        }
    }


    public static void main(String[] args){
       getEntropy1();
    }

}

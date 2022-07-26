import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class generateNewEntropy {

    public static ArrayList<String> calWithProgress(String path){
        ArrayList<String> result = new ArrayList<>();
        File file = new File(path);
        ArrayList<String> paths = new ArrayList<>();
        for(File oneFile: file.listFiles()){
            paths.add(oneFile.getPath());
        }
        Collections.sort(paths);
        for(String tempPath:paths){

          File f = new File(tempPath);
          if(f.isDirectory()){
              System.out.println(tempPath);
              result.add(tempPath);
          }

        }
        return result;
    }
    public static ArrayList<String> getInfo (String path){
        ArrayList<String> info = getFileNO.readFile(path);
        return info;
        /*
            ArrayList<Double> res = new ArrayList<>();
        for(int i=0;i< dev.size();i++){
            String[] eachLine = dev.get(i).split(",");
            String type = eachLine[0];
            double mean = Double.parseDouble(eachLine[1]);
            double devi = Double.parseDouble(eachLine[2]);

        }
         */
    }
    public static void generateNewEntropy(ArrayList<String> lineEntropyType, ArrayList<String> devData, String finalPath){
        File ff = new File(finalPath);
        if(ff.exists()){
            ff.delete();
        }
        for(int i=0;i<lineEntropyType.size();i++){
            String[] eachLine = lineEntropyType.get(i).split(",");
            String lineNo = eachLine[0];
            double origEntropy = Double.parseDouble(eachLine[1]);
            String origType = eachLine[2];

            for(int j=0;j<devData.size();j++){
                String[] eachDevData = devData.get(j).split(",");
                String devType = eachDevData[0];
                double utype = Double.parseDouble(eachDevData[1]);
                double dev = Double.parseDouble(eachDevData[2]);
                double newEntropy=0.0;
                if(origType.equals(devType)){
                    if(dev == 0.0){
                        newEntropy =0.0;
                    }else{
                        newEntropy =(-(origEntropy)-utype)/dev;
                        System.out.println(lineNo + " " + newEntropy);
                    }

                    getFileNO.appendFile(lineEntropyType.get(i)+","+newEntropy+"\n",finalPath);
                }
            }
        }
    }

    public static void outputNewEntropy(String path){
        ArrayList<String> devData = getInfo(path +"/averageEntropy.txt");
        ArrayList<String> res = calWithProgress(path);
        for(int i=0;i< res.size();i++){
            String eachPath = res.get(i);
            File file = new File(eachPath);
            File tf = new File(eachPath+"/newLineEntropy.txt");
            if(tf.exists()){
                tf.delete();
            }
            for(File each : file.listFiles()){
                String tempPath = each.getPath();
                if(tempPath.endsWith("lineEntropyType.txt")){
                    ArrayList<String> origEntropy = getFileNO.readFile(tempPath);
                    generateNewEntropy(origEntropy,devData,eachPath+"/newLineEntropy.txt");

                }
            }
        }
    }

    public static void geter(){
        String[] versions = new String[]{"free","buggy","fix"};

        for(int i=0;i<setting.bugids.length;i++){
            int bugid = setting.bugids[i];
            for(int j = 0;j< versions.length;j++){
                String version = versions[j];
                String path="./afterGenerationEntropy/"+setting.projectName+"/"+setting.projectName+"_"+bugid+"_"+version+"_entropy/";
                outputNewEntropy(path);

            }
        }
    }
    public static void main(String[] args){
        System.out.println("---------start---------");
        geter();
    }

}

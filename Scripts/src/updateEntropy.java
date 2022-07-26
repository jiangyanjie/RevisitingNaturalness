import java.io.File;
import java.util.*;

public class updateEntropy {

    public static ArrayList<String> calWithProgress(String path){
        ArrayList<String> result = new ArrayList<>();
        File file = new File(path);
        ArrayList<String> paths = new ArrayList<>();
        for(File oneFile: file.listFiles()){
            paths.add(oneFile.getPath());
        }
        Collections.sort(paths);
        for(String tempPath:paths){

          File entropyFile = new File(tempPath);
          for(File each : entropyFile.listFiles()){
              if(each.getPath().endsWith("lineEntropyType.txt")){
                  System.out.println(each.getPath());
                  result.add(each.getPath());
              }
          }
        }
        return result;
    }


    public static ArrayList<String> uType(String path){
        ArrayList<String> utype = new ArrayList<>();
        ArrayList<String> filePathList = calWithProgress(path);
        Map<String,Integer> type = new HashMap<>();
        Map<String,Double> res = new HashMap<>();

        for(int i=0;i< filePathList.size();i++){
            String entropyFile = filePathList.get(i);

            ArrayList<String> context = getFileNO.readFile(entropyFile);
            for(int j=0;j< context.size();j++){
                String[] eachLine = context.get(j).split(",");
                String eachEntropy = eachLine[1];
                String eachType = eachLine[2];
                if(type.containsKey(eachType)){
                    int value = type.get(eachType)+1;
                    type.put(eachType,value);
                }else{
                    type.put(eachType,1);
                }
                if(res.containsKey(eachType)){
                    double totEnt = res.get(eachType)+Double.parseDouble(eachEntropy);
                    res.put(eachType,totEnt);
                }else{
                    res.put(eachType,-0.0);
                }
            }
        }

        for(String key: type.keySet()){
//         System.out.println(key + "==::" + type.get(key));
            int specNum = type.get(key);
            double specEnt = res.get(key);
            double aveEnt = -specEnt/specNum;
            System.out.println(key + "::" + aveEnt);
            utype.add(key + "," + aveEnt);
        }
        return utype;
    }


    public static Map<String, ArrayList<Double>> stdDev(String path){
        ArrayList<String> filePathList = calWithProgress(path);
        Map<String, ArrayList<Double>> dev = new HashMap<>();

        for(int i=0; i< filePathList.size(); i++){
            String entropyFile = filePathList.get(i);
            ArrayList<String> context = getFileNO.readFile(entropyFile);
            for(int j=0;j<context.size();j++){
                String[] eachLine = context.get(j).split(",");
                String key = eachLine[2];
                Double value = -(Double.parseDouble(eachLine[1]));
                if(dev.containsKey(key)){
                    ArrayList<Double> values = dev.get(key);
                    values.add(value);
                    dev.put(key,values);
                }else{
                    dev.put(key,new ArrayList<Double>(Arrays.asList(value)));
                }
            }
        }

        for(String key : dev.keySet()){
//         System.out.println(key + "::::" + dev.get(key));
//         System.out.println("====");
        }
        return dev;
    }

    public static ArrayList<String> calDev(Map<String, ArrayList<Double>> data){
        ArrayList<String> result = new ArrayList<>();
        for(String key : data.keySet()){
            String key1 = key;
            double value1 = POP_STD_DEV(listToArray(data.get(key)));
            System.out.println(key1 + ":::" + value1);
            result.add(key1+","+value1);
        }
        return result;

    }
    public static double[] listToArray(ArrayList<Double> mapKey){
        double[] array = new double[mapKey.size()];
        for(int i=0;i<mapKey.size();i++){
            array[i] = mapKey.get(i);
        }
        return array;

    }
    public static double POP_STD_DEV(double[] data){
        double std_dev;
        std_dev = Math.sqrt(POP_Variance(data));
        return std_dev;
    }

    public static double POP_Variance(double[] data){
        double variance = 0;
        for(int i=0;i<data.length;i++){
            variance = variance + (Math.pow((data[i] - Mean(data)),2));
        }
        variance = variance /data.length;
        return variance;
    }

    public static double Mean(double[] data){
        double mean = 0;
        mean = Sum(data) /data.length;
        return mean;
    }

    public static double Sum(double[] data){
        double sum = 0;
        for(int i=0; i< data.length; i++){
            sum = sum +data[i];
        }
        return sum;
    }

    public static void updateEntropy(String path,String filePath){
        File fileP = new File(filePath);
        if(fileP.exists()){
            fileP.delete();
        }
        ArrayList<String> res1 = uType(path);
        Map<String, ArrayList<Double>> temp = stdDev(path);
        ArrayList<String> res2 = calDev(temp);
        for(int i=0;i< res1.size();i++){
            String key1 = res1.get(i).split(",")[0];
            String value1 = res1.get(i).split(",")[1];
            for(int j=0;j<res2.size();j++){
                String key2 = res2.get(j).split(",")[0];
                String value2 = res2.get(j).split(",")[1];
                if(key1.equals(key2)){
                    String rR = key1 + "," + value1 + "," + value2;
                    System.out.println(rR);

                    getFileNO.appendFile(rR+"\n",filePath);
                }
            }
        }

    }

    public static void main(String[] args){
        System.out.println("-----start------");

        String[] versions = new String[]{"free","buggy","fix"};

        for(int i=0;i<setting.bugids.length;i++){
            int bugid = setting.bugids[i];
            for(int j = 0;j< versions.length;j++){
                String version = versions[j];
                String path="./afterGenerationEntropy/"+setting .projectName+"/"+setting.projectName+"_"+bugid+"_"+version+"_entropy/";
                String filePath="./afterGenerationEntropy/"+setting.projectName+"/"+ setting.projectName+"_"+bugid+"_"+version+"_entropy/averageEntropy.txt";
                try{
                    updateEntropy(path,filePath);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}

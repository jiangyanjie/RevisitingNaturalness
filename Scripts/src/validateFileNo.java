import java.util.ArrayList;

public class validateFileNo {
    public static void main(String[] args){
        System.out.println("------start------");
        ArrayList<String> res = getFileNO.readFile("./INFO.txt");

        for(int i=0;i< res.size();){
          String[] temp1= res.get(i).split(",");
          String[] temp2 = res.get(i+1).split(",");
          String[] temp3 = res.get(i+2).split(",");
          String project = temp1[0];
          String bugid = temp1[1];
          String fileNO1 = temp1[3];
          String fileNO2 = temp2[3];
          String fileNO3 = temp3[3];
          String result = project +","+bugid +","+ fileNO1 +"," + fileNO2 +"," + fileNO3;
          System.out.println(result);
          getFileNO.appendFile(result+"\n","./INFO1.txt");
          i+=3;

        }
    }



}

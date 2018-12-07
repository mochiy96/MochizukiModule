/*
CSVファイルからJSONファイルを作るクラスです。
食品データ構造を指定する場合
[食品名,値段,赤,緑,黄,エネルギー,タンパク質,脂質,炭水化物,食塩相当量,カルシウム,野菜量,説明]
のようにして下さい。
 */

import java.io.*;

public class MakeJSON {

    synchronized public static void main(String[] args) {

        String rfilename = "Food.csv";	//読み込みファイル名
        String wfilename = "Food.json";		//JSONファイル名
        int mode = 0;						//食品データ構造=0 その他=2

        /* ファイルの確認 */
        File rfile = new File(rfilename);
        File wfile = new File(wfilename);
        if (rfile.exists()) {	//読み込みファイルの存在確認
        }else {
            System.out.println("エラー：ファイルが存在しません");
            System.exit(0);
        }
        if(wfile.exists()) {	//書き込みファイル先の確認
            wfile.delete();
        }else {
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rfile),"UTF-8"));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wfile),"UTF-8")));

            String line;	//読み込む列
            int cnt = 0;	//読み込まれた列
            String[] datanames = null;	//データ名
            String[] data = null;		//データの要素

            if(mode == 0) {
                pw.println("[");
                while((line = br.readLine()) != null) {
                    if(cnt == 0) {
                    }else if(cnt > 1) {
                        pw.println("	{");
                        pw.println("		\"name\":\"" + data[0] + "\",");
                        pw.println("		\"cost\":\"" + data[1] + "\",");
                        pw.println("		\"foodColors\":{");
                        pw.println("			\"red\":\"" + data[2] + "\",");
                        pw.println("			\"green\":\"" + data[3] + "\",");
                        pw.println("			\"yellow\":\"" + data[4] + "\"");
                        pw.println("		},");
                        pw.println("		\"nutrients\":{");
                        pw.println("			\"energy\":\"" + data[5] + "\",");
                        pw.println("			\"protein\":\"" + data[6] + "\",");
                        pw.println("			\"lipid\":\"" + data[7] + "\",");
                        pw.println("			\"carbohydrate\":\"" + data[8] + "\",");
                        pw.println("			\"salt\":\"" + data[9] + "\",");
                        pw.println("			\"calcium\":\"" + data[10] + "\",");
                        pw.println("			\"vegetable\":\"" + data[11] + "\"");
                        pw.println("		},");
                        pw.println("		\"description\":\"" + data[12] + "\"");
                        pw.println("	},");
                    }
                    data = line.split(",");
                    cnt++;
                }
                pw.println("	{");
                pw.println("		\"name\":\"" + data[0] + "\",");
                pw.println("		\"cost\":\"" + data[1] + "\",");
                pw.println("		\"foodColors\":{");
                pw.println("			\"red\":\"" + data[2] + "\",");
                pw.println("			\"green\":\"" + data[3] + "\",");
                pw.println("			\"yellow\":\"" + data[4] + "\"");
                pw.println("		},");
                pw.println("		\"nutrients\":{");
                pw.println("			\"energy\":\"" + data[5] + "\",");
                pw.println("			\"protein\":\"" + data[6] + "\",");
                pw.println("			\"lipid\":\"" + data[7] + "\",");
                pw.println("			\"carbohydrate\":\"" + data[8] + "\",");
                pw.println("			\"salt\":\"" + data[9] + "\",");
                pw.println("			\"calcium\":\"" + data[10] + "\",");
                pw.println("			\"vegetable\":\"" + data[11] + "\"");
                pw.println("		},");
                pw.println("		\"description\":\"" + data[12] + "\"");
                pw.println("	}");
                pw.println("]");
            }else if(mode == 2) {
                pw.println("[");
                while((line = br.readLine()) != null) {
                    if(cnt == 0) {
                        datanames = line.split(",");
                    }else if(cnt > 1) {
                        pw.println("	{");
                        for(int i = 0; i < datanames.length; i++){
                            if(i == (datanames.length-1)){
                                pw.println("        \"" + datanames[i] + "\":\"" + data[i] + "\"");
                            }else {
                                pw.println("        \"" + datanames[i] + "\":\"" + data[i] + "\",");
                            }
                        }
                        pw.println("    },");
                    }
                    data = line.split(",");
                    cnt++;
                }
                pw.println("	{");
                for(int i = 0; i < datanames.length; i++){
                    if(i == (datanames.length-1)){
                        pw.println("        \"" + datanames[i] + "\":\"" + data[i] + "\"");
                    }else {
                        pw.println("        \"" + datanames[i] + "\":\"" + data[i] + "\",");
                    }
                }
                pw.println("    }");
                pw.println("]");
            }else {
                System.out.println("エラー：このモードは存在しません");
                System.exit(0);
            }

            pw.close();
            br.close();
        } catch(FileNotFoundException e) {
            System.out.println(e);
        } catch(IOException e) {
            System.out.println(e);
        }

    }

}

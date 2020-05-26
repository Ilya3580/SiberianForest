package com.example.siberianforest;
import android.content.Context;
import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Parsing {

    private ArrayList<ArrayList<PriceCatalog>> priceCatalogLast;

    String link;
    public void start(Context context) {
        priceCatalogLast = new ArrayList<>();
        link = "http://slp2000.ru/";
        BufferedReader buf = readBuffer(link);
        String[] mas = returnLink(buf);
        /*for (int i = 0; i != mas.length; i++) {
            Log.d("TAGAZ", mas[i]);
        }*/
        ArrayList<ArrayList<PriceCatalog>> priceCatalog = new ArrayList<>();
        for(int j = 0; j!= mas.length; j++) {
            checkPrice(readBuffer(mas[j]));

        }
        String stringPriceCatalog = "";
        for (int i = 0; i != priceCatalogLast.size(); i++) {
            if(i != 0)
                stringPriceCatalog +="------------------    ";
            for (int j = 0; j != priceCatalogLast.get(i).size(); j++) {
                //Log.d("TAGA", priceCatalogLast.get(i).get(j).toString());
                stringPriceCatalog +=priceCatalogLast.get(i).get(j).toString() + "####";
            }
        }

        writeToFile(stringPriceCatalog, context);

        //return priceCatalog;

    }

    private void writeToFile(String stringPriceCatalog, Context context) {
        try {
            FileOutputStream fileOutput = context.openFileOutput("textparametr.txt", MODE_PRIVATE);
            fileOutput.write(stringPriceCatalog.getBytes());
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader readBuffer(String link) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;
        try {
            assert url != null;
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


        BufferedReader buf = null;
        try {
            assert connection != null;
            buf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buf;
    }

    private String[] returnLink(BufferedReader buf) {
        String inputeLine = "";

        StringBuilder str = new StringBuilder();
        boolean flag = false;
        while (true) {

            try {
                if (null == (inputeLine = buf.readLine())) {
                    break;
                } else {

                    if (inputeLine.contains("<div class=\"panel_right\">")) {
                        flag = false;
                    }
                    if (flag) {
                        str.append("###").append(inputeLine);
                    }
                    if (inputeLine.contains("<a class=\"sar\" href=\"/catalog/\">")) {
                        flag = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] masRet;
        String[] mas = str.toString().split("\">###");
        for (int i = 0; i != mas.length; i++) {
            String[] mas1 = mas[i].split("href=\"");
            mas[i] = "http://slp2000.ru" + mas1[mas1.length - 1];
            //Log.d("TAGA", mas[i] + "    "+i);
        }

        masRet = new String[mas.length - 1];
        for (int i = 0; i != masRet.length; i++) {
            masRet[i] = mas[i];
        }

        return masRet;
    }

    private void checkPrice(BufferedReader buf) {
        String inputeLine = "";

        ArrayList<String> arrayList = new ArrayList<>();
        String str = "";
        boolean flag = false;
        while (true) {
            try {
                if (null == (inputeLine = buf.readLine())) {
                    break;
                } else {
                    if (inputeLine.contains("<td class=\"imgprice\"><a")) {
                        flag = true;
                    }
                    if (inputeLine.contains("</tr>")) {
                        if(flag)
                        {
                            arrayList.add(str);
                            str = "";
                        }
                        flag = false;
                    }
                    if (flag) {
                        str += (inputeLine) + ("###");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        ArrayList<PriceCatalog> priceCatalogsList = new ArrayList<>();
        String name = "";
        for(int i = 0; i!= arrayList.size(); i++)
        {
            String[] priceCatalog = splitPrice(arrayList.get(i));

            if(i == 0) {

                priceCatalogsList.add(new PriceCatalog(link + priceCatalog[0], priceCatalog[1], priceCatalog[3], priceCatalog[4], priceCatalog[5], priceCatalog[6]));
            }else{
                if(priceCatalog[1].equals(name)) {
                    priceCatalogsList.add(new PriceCatalog(null, priceCatalog[1], priceCatalog[3], priceCatalog[4], priceCatalog[5], priceCatalog[6]));
                }else{
                    priceCatalogLast.add(priceCatalogsList);
                    priceCatalogsList = new ArrayList<>();
                    priceCatalogsList.add(new PriceCatalog(link + priceCatalog[0], priceCatalog[1], priceCatalog[3], priceCatalog[4], priceCatalog[5], priceCatalog[6]));
                }
            }
            name = priceCatalog[1];

        }
        priceCatalogLast.add(priceCatalogsList);
        return;
    }
    private String[] splitPrice(String string)
    {

        String[] ret = new String[7];
        String[] priceCatalog = string.split(">###");
        priceCatalog[0] = priceCatalog[0].substring(priceCatalog[0].indexOf("src=") + 4);
        ret[6] = priceCatalog[3].substring(priceCatalog[3].indexOf(">") + 1, priceCatalog[3].indexOf("</td"));
        try{
            ret[0] = priceCatalog[0].substring(priceCatalog[0].indexOf("src=") + 3, priceCatalog[0].indexOf("</a")-2);

            ret[1] = priceCatalog[1].substring(priceCatalog[1].indexOf("htm\">") + 5, priceCatalog[1].indexOf("</a></td"));

            ret[3] = priceCatalog[2].substring(priceCatalog[2].indexOf("ice\">") + 5, priceCatalog[2].indexOf("</td"));
            ret[4] = priceCatalog[4].substring(priceCatalog[4].indexOf("ice\">") + 5, priceCatalog[4].indexOf("</td"));
            ret[5] = priceCatalog[5].substring(priceCatalog[5].indexOf("ice\">") + 5, priceCatalog[5].indexOf("</td"));
        }
        catch(Exception ex){

        }


        return ret;
    }


}

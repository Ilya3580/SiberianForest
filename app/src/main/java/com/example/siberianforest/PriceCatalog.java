package com.example.siberianforest;

import android.util.Log;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;

public class PriceCatalog {
    private String lineImage = null;
    private String name = null;
    private String size = null;
    private String priceM2 = null;
    private String priceM3 = null;
    private String sortTree = null;

    public PriceCatalog() {
    }

    public PriceCatalog(String lineImage, String name,  String size, String priceM2, String priceM3, String sortTree) {

        if(lineImage != null) {
            if(!lineImage.equals("") && !lineImage.equals("null"))
                this.lineImage = lineImage;
        }
        if(name != null)
            if(!name.equals("") && !name.equals("null") && !name.equals("-"))
                this.name = name.trim();
        if(size != null)
            if(!size.equals("") && !size.equals("null") && !size.equals("-"))
                this.size = size.trim();
        if(priceM2 != null)
            if(!priceM2.equals("") && !priceM2.equals("null") && !priceM2.equals("-"))
                this.priceM2 = priceM2.trim();
        if(priceM3 != null)
            if(!priceM3.equals("") && !priceM3.equals("null") && !priceM3.equals("-"))
                this.priceM3 = priceM3.trim();
        if(sortTree != null)
            if(!sortTree.equals("")&& !sortTree.equals("null") && !sortTree.equals("-"))
                this.sortTree = sortTree;
    }

    public String splitPrise(ArrayList<PriceCatalog> lst, String size, String sort)
    {
        String priceM2 = "";
        String priceM3 = "";
        boolean flag = false;
        if(size != null) {
            if (size.equals("размер")) {
                int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE, max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE;
                for (int i = 0; i != lst.size(); i++) {
                    if (lst.get(i).priceM2 != null) {
                        if (Integer.valueOf(lst.get(i).priceM2.indexOf("р. пог.")) != -1) {
                            flag = true;
                        }
                        if (min1 > Integer.valueOf(lst.get(i).priceM2.split(" ")[0])) {
                            min1 = Integer.valueOf(lst.get(i).priceM2.split(" ")[0]);
                        }
                        if (max1 < Integer.valueOf(lst.get(i).priceM2.split(" ")[0])) {
                            max1 = Integer.valueOf(lst.get(i).priceM2.split(" ")[0]);
                        }
                    }
                    if (lst.get(i).priceM3 != null) {
                        if (min2 > Integer.valueOf(lst.get(i).priceM3.split(" ")[0])) {
                            min2 = Integer.valueOf(lst.get(i).priceM3.split(" ")[0]);
                        }
                        if (max2 < Integer.valueOf(lst.get(i).priceM3.split(" ")[0])) {
                            max2 = Integer.valueOf(lst.get(i).priceM3.split(" ")[0]);
                        }
                    }
                }
                if (flag) {
                    if (min2 == Integer.MAX_VALUE || max2 == Integer.MIN_VALUE) {
                        if(min1 == max1)
                        {
                            return min1 + " руб. метр погонный";
                        }
                        return min1 + "-" + max1 + " руб. метр погонный";
                    }
                }
                if (min1 == Integer.MAX_VALUE || max1 == Integer.MIN_VALUE) {
                    if(min2 == max2) {
                        return min2 + " руб. метр куб";
                    }
                    return min2 + "-" + max2 + " руб. метр куб";
                }
                if (min2 == Integer.MAX_VALUE || max2 == Integer.MIN_VALUE) {
                    if(min1 == max1)
                    {
                        return min1 + " руб. метр квадратный";
                    }
                    return min1 + "-" + max1 + " руб. метр квадратный";
                }
                return min1 + "-" + max1 + " руб. метр квадрат; " + min2 + "-" + max2 + " руб. метр куб";
            }
        }
        if(sort != null) {
            if (sort.equals("сорт")) {
                int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE, max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE;
                for (int i = 0; i != lst.size(); i++) {
                    if (lst.get(i).priceM2 != null) {
                        if (Integer.valueOf(lst.get(i).priceM2.indexOf("р. пог.")) != -1) {
                            flag = true;
                        }
                        if (min1 > Integer.valueOf(lst.get(i).priceM2.split(" ")[0])) {
                            min1 = Integer.valueOf(lst.get(i).priceM2.split(" ")[0]);
                        }
                        if (max1 < Integer.valueOf(lst.get(i).priceM2.split(" ")[0])) {
                            max1 = Integer.valueOf(lst.get(i).priceM2.split(" ")[0]);
                        }
                    }
                    if (lst.get(i).priceM3 != null) {
                        if (min2 > Integer.valueOf(lst.get(i).priceM3.split(" ")[0])) {
                            min2 = Integer.valueOf(lst.get(i).priceM3.split(" ")[0]);
                        }
                        if (max2 < Integer.valueOf(lst.get(i).priceM3.split(" ")[0])) {
                            max2 = Integer.valueOf(lst.get(i).priceM3.split(" ")[0]);
                        }
                    }
                }
                if (flag) {
                    if (min2 == Integer.MAX_VALUE || max2 == Integer.MIN_VALUE) {
                        if(min1 == max1)
                        {
                            return min1 + " руб. метр погонный";
                        }
                        return min1 + "-" + max1 + " руб. метр погонный";
                    }
                }
                if (min1 == Integer.MAX_VALUE || max1 == Integer.MIN_VALUE) {
                    if(min2 == max2) {
                        return min2 + " руб. метр куб";
                    }
                    return min2 + "-" + max2 + " руб. метр куб";
                }
                if (min2 == Integer.MAX_VALUE || max2 == Integer.MIN_VALUE) {
                    if(min1 == max1)
                    {
                        return min1 + " руб. метр квадратный";
                    }
                    return min1 + "-" + max1 + " руб. метр квадратный";
                }
                return min1 + "-" + max1 + " руб. метр квадрат; " + min2 + "-" + max2 + " руб. метр куб";
            }
        }
        for(int i = 0;i != lst.size(); i++)
        {
            if(lst.get(i).size == null)
            {
                if(lst.get(i).getSortTree().equals(sort))
                {
                    priceM2 = lst.get(i).getPriceM2();
                    priceM3 = lst.get(i).getPriceM3();
                }
            }else if(lst.get(i).sortTree == null)
            {
                if(helpSplitStringPrice(lst.get(i).getSize(), size))
                {
                    priceM2 = lst.get(i).getPriceM2();
                    priceM3 = lst.get(i).getPriceM3();
                }
            }
            else if(lst.get(i).getSize() != null) {
                if(helpSplitStringPrice(lst.get(i).getSize(), size) && lst.get(i).getSortTree().equals(sort))
                {
                    priceM2 = lst.get(i).getPriceM2();
                    priceM3 = lst.get(i).getPriceM3();
                }
            }

        }

        String string = "";

        if(priceM2 != null)
        {
            string +=priceM2;
        }
        if(priceM3 != null)
        {
            string +=" " + priceM3;
        }

        return string;


    }

    private boolean helpSplitStringPrice(String str1, String str2)
    {
        if(str1.equals(str2))
        {
            return true;
        }
        if(str1.indexOf("(") != -1)
        {
            String[] str2Mas = str2.split(" X ");
            String[] mas = str1.split(" X ");

            boolean flag1 = false;
            boolean flag = false;
            if(mas[1].indexOf("(") != -1)
            {
                flag1 = true;
                int a1 = Integer.valueOf(mas[1].split("-")[0].trim().substring(1)),
                        a2 = Integer.valueOf(mas[1].split("-")[mas[1].split("-").length-1].trim().substring(0,mas[1].split("-")[mas[1].split("-").length-1].trim().length()-1));

                int number = Integer.valueOf(str2Mas[1]);
                if(number > a1 && number < a2 || number == a1 || number == a2)
                {
                    flag = true;

                }

            }

            int a1 = Integer.valueOf(mas[2].split("-")[0].trim().substring(1)),
                    a2 = Integer.valueOf(mas[2].split("-")[1].trim().substring(0,mas[2].split("-")[1].trim().length()-1));
            if((mas[0] + mas[1]).indexOf(str2Mas[0] + str2Mas[1]) !=-1)
            {
                int number = Integer.valueOf(str2Mas[2]);
                if(number > a1 && number < a2 || number == a1 || number == a2)
                {
                    if(flag1){
                        if(flag) {
                            return true;
                        }
                    }else {
                        return true;
                    }
                }
            }
        }
        if(str1.indexOf(";") != -1)
        {
            String[] str2Mas = str2.split(" x ");
            String[] mas = str1.split(" x ");
            if((mas[0] + mas[1]).indexOf(str2Mas[0] + str2Mas[1]) !=-1) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ArrayList<PriceCatalog>> splitString(String string)
    {
        String mas[] = string.split("------------------");
        ArrayList<ArrayList<PriceCatalog>> parentList = new ArrayList<>();

        for(int i = 0; i!= mas.length; i++)
        {

            ArrayList<PriceCatalog> arrayList = new ArrayList<>();
            String[] splitMas = mas[i].split("####");
            for(int j = 0; j!=splitMas.length; j++) {
                String[] lastSplit = splitMas[j].split("@@@");
                arrayList.add(new PriceCatalog(lastSplit[0].trim(), lastSplit[1].trim(), lastSplit[2].trim(), lastSplit[3].trim(), lastSplit[4].trim(), lastSplit[5].trim()));
            }
            parentList.add(arrayList);

        }

        return parentList;


    }

    public String[] splitSize(ArrayList<PriceCatalog> lst)
    {
        if(lst.get(lst.size() - 1).getSize() == null)
            return null;
        if(lst.get(lst.size() - 1).getSize().indexOf(";") != -1)
        {
            ArrayList<String> arrayList = new ArrayList<>();
            String[] mas = new String[lst.size()];
            for(int i = 0; i!= lst.size(); i++)
            {
                mas[i] = lst.get(i).getSize();
            }
            for(int i = 0; i!= lst.size(); i++)
            {
                String[] str = mas[i].split(";");
                String zag = (str[0].split(" x ")[0]) + " X " + (str[0].split(" x ")[1]) + " X ";
                arrayList.add(str[0]);
                for(int j = 1; j!= str.length; j++)
                {
                    arrayList.add(zag + str[j]);
                }

            }

            String[] str = new String[arrayList.size() + 1];
            str[0] = "размер";
            for(int i = 1; i!=str.length;i++)
            {
                str[i] = arrayList.get(i-1);
            }

            return str;
        }
        if(lst.get(lst.size() - 1).getSize().indexOf("(") != -1)
        {

            String[] str = null;

            for(PriceCatalog item:lst)
            {
                String[] help;
                if(str == null)
                {
                    str = helpSplitSize(item.getSize());
                    continue;
                }
                if(str.length < (help = helpSplitSize(item.getSize())).length)
                {
                    str = help;
                }
            }

            String[] str1 = new String[str.length + 1];
            str1[0] = "размер";
            for(int i = 1; i!=str1.length;i++)
            {
                str1[i] = str[i-1];
            }
            return destroyBliz(str1);
        }
        if(lst.get(0).getSize().equals(lst.get(lst.size()-1).getSize()))
        {
            String[] str = new String[lst.size() + 1];
            str[0] = "размер";
            for(int i = 1; i!=str.length;i++)
            {
                str[i] = lst.get(i-1).getSize();
            }
            return destroyBliz(str);
        }
        if(lst.get(lst.size() - 1).getSize().indexOf("(") == -1)
        {
            String[] masL = new String[lst.size() + 1];
            masL[0] = "размер";
            for(int i = 1; i!= masL.length; i++)
            {
                masL[i] = lst.get(i-1).getSize();
            }
            return destroyBliz(masL);
        }

        return null;
    }

    private String[] destroyBliz(String[] str)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0; i!=str.length; i++)
        {
            boolean flag = true;
            for(int j = 0; j!=arrayList.size(); j++)
            {
                if(arrayList.get(j).equals(str[i]))
                {
                    flag = false;
                }
            }
            if(flag)
            {
                arrayList.add(str[i]);
            }
        }
        String[] str1 = new String[arrayList.size()];

        for(int i = 0; i != str1.length; i++)
        {
            str1[i] = arrayList.get(i);
        }
        return str1;

    }

    public String[] splitSort(ArrayList<PriceCatalog> lst)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        if(lst.get(0).sortTree == null)
        {
            return null;
        }
        for(int i = 0;i!=lst.size();i++)
        {
            boolean flag = true;
            for(int j = 0; j!=arrayList.size(); j++)
            {
                if(arrayList.get(j).equals(lst.get(i).sortTree))
                {
                    flag = false;
                }
            }
            if(flag)
            {
                arrayList.add(lst.get(i).sortTree);
            }
        }
        String[] mas = new String[arrayList.size()+1];
        mas[0] = "сорт";
        for(int i = 1; i!=mas.length;i++)
        {
            mas[i] = arrayList.get(i-1);
        }
        return destroyBliz(mas);
    }

    private String[] helpSplitSize(String strr)
    {
        String[] str = strr.split(" X ");
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        for(int i = 0; i != str.length; i++)
        {
            arrayLists.add(helpSplitString(str[i]));
        }

        str = new String[arrayLists.get(0).size() * arrayLists.get(1).size() * arrayLists.get(2).size()];

        int i1 = 0,i2 = 0,i3 = 0;
        for(int i = 0; i!=str.length; i++)
        {

            str[i] = arrayLists.get(0).get((i/arrayLists.get(2).size())/arrayLists.get(1).size()) + " X "
                    + arrayLists.get(1).get(i/arrayLists.get(2).size()%arrayLists.get(1).size()) + " X "
                    + arrayLists.get(2).get(i%arrayLists.get(2).size());

        }

        return str;
    }

    private ArrayList<String> helpSplitString(String str)
    {
        if(str.split(" - ").length == 1)
        {
            ArrayList<String> a = new ArrayList<>();
            a.add(str);
            return a;
        }
        int string1 = Integer.valueOf(str.split(" - ")[0].substring(1)), string2;
        String string3 = str.split(" - ")[str.split(" - ").length-1];
        string2 = Integer.valueOf(string3.substring(0,string3.length()-1));
        int helpStr = string1;
        int step = 5;
        while (helpStr != 0)
        {

            step *=10;


            helpStr/=10;
        }
        step = step/100;
        ArrayList<String> arrayList = new ArrayList<>();
        for(int  i = 0; string1 + (step)*i <= string2; i++)
        {
            arrayList.add(String.valueOf(string1 + (step)*i));
        }

        return arrayList;

    }


    public void setLineImage(String lineImage) {
        this.lineImage = lineImage;
    }

    public void setSortTree(String sortTree) {
        this.sortTree = sortTree;
    }

    public String getLineImage() {
        return lineImage;
    }

    public String getSortTree() {
        return sortTree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPriceM2() {
        return priceM2;
    }

    public void setPriceM2(String priceM2) {
        this.priceM2 = priceM2;
    }

    public String getPriceM3() {
        return priceM3;
    }

    public void setPriceM3(String priceM3) {
        this.priceM3 = priceM3;
    }

    @NonNull
    @Override
    public String toString() {
        return lineImage + "@@@" + name + "@@@" +size+ "@@@" +priceM2+ "@@@" +priceM3 + "@@@" + sortTree;
    }
}

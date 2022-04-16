package Driver;

import Model.TVShow;
import ShowList.ShowList;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ProcessWishlist {
    static final String basePath = System.getenv().get("PWD");
    static final String guidePath = basePath + "/src/data/TVGuide.txt";
    static final String intrestsPath = basePath + "/src/data/Interest.txt";

    public static String readFileContents(String path) throws FileNotFoundException {
        ArrayList<String> contents = new ArrayList<>();
        StringBuilder bldr = new StringBuilder();
        try {
            FileInputStream inputStream = new FileInputStream(path);
            Scanner snc = new Scanner(inputStream);
            int i = 0;
            while (snc.hasNext()) {
                String line = snc.nextLine();
                if (line.isEmpty()) {
                   bldr.append(",");
                } else{
                    bldr.append(line);
                }
            }
            snc.close();
        } catch (FileNotFoundException exception) {
            throw new FileNotFoundException("Cannot locate the file in the mentioned path");
        }
        return bldr.toString();
    }

    public static void addShows(String file, ShowList lst)  {
        if (file.equals("tv")) {
            try {
                System.out.println(guidePath);
                String contents = readFileContents(guidePath);
                String[] records = contents.split(",");
                ArrayList<String[]> values = new ArrayList<>();
                for(String record: records) {
                    String[] content = record.split(" ");
                    values.add(content);
                }
                for (String[] val: values) {
                    String showId = val[0];
                    String showName = val[1];
                    showName = showName.substring(0, showName.length() - 1);
                    String startingValue = val[2].substring(0, val[2].length() - 1);
                    double startTime = Double.parseDouble(startingValue);
                    double endTime = Double.parseDouble(val[3]);
                    TVShow currentShow = new TVShow(showId, showName, startTime, endTime);
                    lst.addToStart(currentShow);
                }

            } catch (FileNotFoundException exception) {
                System.out.println("File is not found in the system. ");
            }
        }
    }

    private static List<String[]> interestsFile(String location) {
        StringBuilder contents = new StringBuilder();
        try {
            FileInputStream inputStream = new FileInputStream(location);
            Scanner snc = new Scanner(inputStream);
            while (snc.hasNext()) {
                String line = snc.nextLine();
                contents.append(line);
                contents.append(" ");
            }
            String content = contents.toString();
            String[] contentList = content.split("Wishlist");
            List<String[]> finalContent = new ArrayList<>();
            for (String lstElement: contentList) {
                String val = lstElement.replace("Watching", "");
                val = val.replace(" ", ",");
                String[] innerLst = val.split(",");
                // remove the first empty element from the list.
                String[] innerListFinal = new String[innerLst.length - 1];
                int k = 0;
                for(String member: innerLst) {
                    if (!member.isEmpty()) {
                        innerListFinal[k] = member;
                        k++;
                    }
                }
                finalContent.add(innerListFinal);
            }

            return finalContent;
        } catch (FileNotFoundException exception) {
            System.out.println("File location is not found in the system. ");
        }
        return null;
    }

    private static void process() {
        try {
            List<String[]>  contents = interestsFile(intrestsPath);
            if (contents != null) {
                String[] watching = contents.get(0);
                String[] intrested = contents.get(1);
                System.out.println("Watching " + Arrays.toString(watching));
                System.out.println("Intrested in " + Arrays.toString(intrested));
            } else {
                System.out.println("content is set to null");
            }
        } catch (Exception e) {
            System.out.println("Exception happened");
        } finally {
            System.out.println("Processed the interest file");
        }
    }

    private static void performSearchOperation(ShowList lst1, Scanner snc) {
        boolean isShowing = true;
        System.out.println("To exit the search press -1 on the keyboard");

        while (isShowing) {
            System.out.println("Enter the show ID");
            String showId = snc.next();
            if (showId.equals("-1")) {
                isShowing = false;
            } else {
                ShowList.ShowNode node =  lst1.find(showId);
                if (node == null) {
                    System.out.println("No tv show with the id " + showId + " is found in the system.");
                } else {
                    System.out.println(node);
                }
            }
        }
    }

    public static void main(String[] args)  {
        ShowList lst1 = new ShowList();
        ShowList lst2 = new ShowList();
        addShows("tv", lst1);
        process();
        Scanner snc = new Scanner(System.in);
        performSearchOperation(lst1, snc);
        snc.close();
    }
}

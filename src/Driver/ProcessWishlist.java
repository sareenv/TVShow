package Driver;

import Model.TVShow;
import ShowList.ShowList;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

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

    private static void process(ShowList lst) {
        try {
            List<String[]>  contents = interestsFile(intrestsPath);
            if (contents != null) {
                String[] watching = contents.get(0);
                String[] intrested = contents.get(1);
                System.out.println("Watching " + Arrays.toString(watching));
                System.out.println("Interested in " + Arrays.toString(intrested));
                HashSet<String> canWatch = new HashSet<>();
                for (String watch: watching ) {
                    for (String interest: intrested) {
                        TVShow watchingShow = lst.findWithoutPrint(watch).getShow();
                        TVShow interestShow = lst.findWithoutPrint(interest).getShow();
                        if (watchingShow.isOnSameTime(interestShow).equals("Same time") ||
                                watchingShow.isOnSameTime(interestShow).equals("Some overlap")) {
                            String content = "User cannot watch Interested show:  "
                                    + interestShow.getShowName() + " " +
                                    " Reason: Because overlaps or is on same " +
                                    "time of the currently watching show " +
                                    watchingShow.getShowName() + "\n" + "Watching Show Start Time: "
                                    + watchingShow.getStartTime() + "\n"
                                    + "Watching Show End Time: "  + watchingShow.getEndTime() + "\n"
                                    + "Interested Show: " + watchingShow.getStartTime() + "\n"
                                    + "Interested Show End Time: "  + watchingShow.getEndTime();
                            canWatch.add(content);
                        } else {
                            System.out.println("-----------------------------------");
                            System.out.println("User can watch Interested show:  " + "\n" +
                                    interestShow.getShowName() + "No time conflict with currently watching show "
                                    + watchingShow.getShowName());
                            System.out.println("-----------------------------------");
                        }
                    }
                }

                for (String watch: canWatch) {
                    System.out.println("-------------------");
                    System.out.println(watch);
                    System.out.println("-------------------");
                }

            } else {
                System.out.println("content is set to null");
            }
        } catch (Exception e) {
            System.out.println("Exception happened");
        } finally {
            System.out.println("Finished processing the interest file");
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
                System.out.println("Terminating the program, NOTE: Thanks for using the system ");
            } else {
                ShowList.ShowNode node =  lst1.find(showId);
                if (node == null) {
                    System.out.println("No tv show with the id " + showId + " is found in the system.");
                } else {
                    System.out.println("Found in the show in system with id " + showId);
                }
            }
        }
    }

    public static void main(String[] args)  {
        ShowList lst1 = new ShowList();
        addShows("tv", lst1);
        ShowList lst2 = new ShowList(lst1);
//        lst2.printList(); - just to check - done checked by even debugging.

        System.out.println("Detecting the time conflict");
        System.out.println("----------------------------");
        process(lst2);
        Scanner snc = new Scanner(System.in);
        System.out.println("Do you want to search for tvShow Y/N : ");
        String choice = snc.nextLine();

        if (choice.equals("y") || choice.equals("Y")) {
            performSearchOperation(lst1, snc);
        } else {
            System.out.println("Thanks for using the program");
        }
        snc.close();
    }
}

package Model;

import Interfaces.Watchable;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class TVShow implements Cloneable, Watchable {
    private String showId;
    private String showName;
    private double startTime;
    private double endTime;

    public TVShow(String showId, String showName, double startTime, double endTime) {
        this.showId = showId;
        this.showName = showName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        TVShow show;
        try {
            show = (TVShow) super.clone();
        } catch (InputMismatchException exception) {
            throw new CloneNotSupportedException();
        }
        return show;
    }

    // overloading.
    public TVShow clone(String showId) {
        TVShow show = null;
        try {
            show = (TVShow) this.clone();
            System.out.println("Cloned tv show: " + show);
            System.out.println("Please enter the new show id: ");
            Scanner snc = new Scanner(System.in);
            if (snc.hasNext() && !(snc.next().equals(""))) {
                show.showId = snc.nextLine();
            }
        } catch (CloneNotSupportedException exception) {
            System.out.println("Cloning is not supported! ");
        } catch (Exception exception) {
            System.out.println("Another exception is caused " + exception.getMessage());
        }
        return show;
    }

    public TVShow(TVShow show, String value) {
        this(value, show.showName, show.startTime, show.endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TVShow tvShow = (TVShow) o;
        return Double.compare(tvShow.startTime, startTime) == 0
                && Double.compare(tvShow.endTime, endTime) == 0
                && Objects.equals(showName, tvShow.showName);
    }

    @Override
    public String toString() {
        return "TVShow{" +
                "showId='" + showId + '\'' +
                ", showName='" + showName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(showId, showName, startTime, endTime);
    }


    @Override
    public String isOnSameTime(TVShow show) {
        String result;
        if (this.startTime == show.startTime
                && this.endTime == show.endTime)
            result =  "Same time";
        else if(this.startTime >= show.startTime &&
                this.endTime <= show.endTime)
            result =  "Some overlap";
        else {
            result =  "Different time";
        }
        return result;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }


}

package ShowList;


import Exceptions.NoSuchElementException;
import Model.TVShow;

import java.util.Objects;

public class ShowList {

    private ShowNode head;
    private int size;

    private static class ShowNode implements Cloneable {
        private TVShow show;
        private ShowNode next;

        public ShowNode() {
            this.show = null;
            this.next = null;
        }

        public ShowNode(TVShow show, ShowNode next) {
            this.show = show;
            this.next = next;
        }

        // only makes the deep copy of the current and next node.
        @Override
        protected Object clone() throws CloneNotSupportedException {
            ShowNode node = (ShowNode) super.clone();
            node.next = (ShowNode) node.next.clone();
            node.show = (TVShow) node.show.clone();
            return node;
        }



        public ShowNode(ShowNode node) {
            ShowNode showNode;
            try {
                showNode = (ShowNode) node.clone();
                this.show = showNode.show;
                this.next = showNode.next;
            } catch (CloneNotSupportedException exception) {
                System.out.println("Exception performing the clone. ");
            } finally {
                showNode = null;
            }
        }
    }

    public ShowList(ShowNode head, int size) {
        this.head = head;
        this.size = size;
    }

    public ShowList() {
        this.head = null;
        this.size = 0;
    }

    /*
     * Inserts the TVShow at the starting index of the list.
     * @see TVShow
     * @param show represents the tv show which needs to be inserted at the start of the list
     * */
    void addToStart(TVShow show) {
        // this can not be added to the list.
        if (show != null) {
            if (this.head == null) {
                this.head = new ShowNode(show, null);
            } else {
                ShowNode temp = this.head;
                this.head = new ShowNode(show, temp);
            }
            this.size += 1;
        }
    }


    void printList() {
        if (this.head == null) {
            System.out.println("List has no items to print.");
        } else {
            ShowNode tempNode = this.head;
            while (tempNode != null) {
                System.out.println(tempNode.show);
                tempNode = tempNode.next;
            }
        }
    }

    // also need to keep track of the operations
    ShowNode find(String showId) {
        ShowNode currentShow = this.head;
        int operations = 0;
        while (currentShow != null) {
            if (Objects.equals(currentShow.show.getShowId(), showId)) {
                System.out.println("Operations required was " + operations);
                return currentShow;
            }
            currentShow = currentShow.next;
            operations += 1;
        }
        System.out.println("Operations required was " + operations);
        return null;
    }

    ShowNode getAtIndex(int index) {
        if (index == 0) { return this.head; }
        if (index > this.size) { return null; }
        if (index < 0) { return null; }
        ShowNode currentShow = this.head;
        int currentIndex = 0;
        while (currentShow != null && currentIndex != index)  {
            currentShow = currentShow.next;
            currentIndex += 1;
        }
        return currentShow;
    }


    void insertAtIndex(TVShow show, int index) throws NoSuchElementException {
        if (index < 0 || index > this.size) { throw new IndexOutOfBoundsException(); }
        if (index == 0) { addToStart(show); return; }
        int currentIndex = 0;
        ShowNode currentShow = this.head;
        while (currentIndex  != index - 1) {
            currentShow = currentShow.next;
            currentIndex += 1;
        }
        ShowNode tempShow = currentShow.next;
        currentShow.next = new ShowNode(show, tempShow);
        this.size += 1;
        tempShow = null;
    }

    /*
    * Check if the showID is present in the list.
    * @see ShowNode
    * @param id represents the showId of the showNode.
    * @return boolean
    * */
    public boolean contains(String id) {
        ShowNode currentShow = this.head;
        while (currentShow != null) {
            if (Objects.equals(currentShow.show.getShowId(), id)) {
                return true;
            }
            currentShow = currentShow.next;
        }
        return false;
    }

    /*
     * Deletes the
     * @see ShowNode
     * @param index position of showNode in the list which needs to be removed
     * @return boolean
     * */
    public boolean deleteFromIndex(int index) {
        if (index < 0 || index >= this.size) { return false; }
        if (index == 0) {
            deleteFromStart();
        } else {
            ShowNode prevNode = getAtIndex(index - 1);
            prevNode.next = getAtIndex(index).next;
            this.size -= 1;
        }
        return true;
    }

    public void deleteFromStart() {
        if (this.size == 0) { return; }
        this.head = this.head.next;
        this.size -= 1;
    }

    public void replaceAtIndex(TVShow show, int index) {
        if (index >= this.size || index < 0) {
            System.out.println("Operation cannot be performed as " +
                    "invalid index is provided!");
            return;
        }
        ShowNode showNode = getAtIndex(index);
        showNode.show = show;
    }


    public static void main(String[] args)  {
        ShowList lst = new ShowList();
        TVShow show = new TVShow("123", "friends", 20.00, 21.00);
        TVShow show2 = new TVShow("8943", "Avengers", 23.00, 21.00);
        TVShow show3 = new TVShow("83943", "Dr.Stain", 20.00, 21.00);
        TVShow show4 = new TVShow("as12", "Ashish Chanchlani", 32.00, 40.00);
        lst.addToStart(show);
        lst.addToStart(show2);
        lst.addToStart(show3);
        try {
            int insertionIndex = 1;
            lst.insertAtIndex(show4, insertionIndex);
        } catch (NoSuchElementException exception) {
            System.exit(1);
        }
        ShowNode node = lst.getAtIndex(1);
        if (node != null) { System.out.println(node.show); }
        else { System.out.println("Node is null"); }
    }

}

package ShowList;

import Exceptions.NoSuchElementException;
import Model.TVShow;

import java.util.Objects;

/**
 * ShowList class is an implementation of the linked list with custom functionality of handling the tv shows with
 * various operations added to it such as deleting from the start of the linked list,
 */

public class ShowList  {

    private ShowNode head;
    private int size;

    public int getSize() {
        return size;
    }

    public static class ShowNode implements Cloneable {
        private TVShow show;
        private ShowNode next;

        public ShowNode() {
            this.show = null;
            this.next = null;
        }

        public TVShow getShow() {
            return show;
        }

        public ShowNode getNext() {
            return next;
        }



        public ShowNode(TVShow show, ShowNode next) {
            this.show = show;
            this.next = next;
        }

         @Override
         public String toString() {
             return "Node with show " + show;
         }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            ShowNode node = (ShowNode) super.clone();
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

    // copy constructor.
    public ShowList(ShowList lst) {
        System.out.println("Cloning the whole list from the existing list - Deep copy");
        System.out.println("---------------------------------------------------------");
        this.head = null;
        this.size = lst.size;
        ShowNode node = lst.head;
        while (node != null) {
            try {
                ShowNode copyNode = (ShowNode) node.clone();
                this.addToStart(copyNode.show);
                node = node.next;
            } catch (CloneNotSupportedException exception) {
                System.out.println("Cloning of the showNode is not supported yet");
            }
        }
    }


    /**
     * Inserts the TVShow at the starting index of the list.
     * @see TVShow
     * @param show represents the tv show which needs to be inserted at the start of the list
     * */
    public void addToStart(TVShow show) {
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

    public ShowNode findWithoutPrint(String showId) {
        ShowNode currentShow = this.head;
        while (currentShow != null) {
            if (Objects.equals(currentShow.show.getShowId(), showId)) {

                return currentShow;
            }
            currentShow = currentShow.next;
        }
        return null;
    }

    public void printList() {
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

    /**
     * Finds the show with the specific show id.
     * @param showId - represents the showID for the search in the linked list.
     * @return ShowNode - returns the specific object which contains the relevant
     * information to the show with the mentioned id.
     */

    public ShowNode find(String showId) {
        ShowNode currentShow = this.head;
        int operations = 0;
        while (currentShow != null) {
            if (Objects.equals(currentShow.show.getShowId(), showId)) {
                operations += 1;
                System.out.println("Operations required was " + operations);
                return currentShow;
            }
            currentShow = currentShow.next;
            operations += 1;
        }
        operations += 1;
        System.out.println("Operations required  to perform search was "
                + operations);
        return null;
    }

    /* PRIVACY LEAK.
    *  - This method can potentially cause the privacy leak as the show pointer is exposed if
    * someone gets the hold of the pointer then - they can manipulate the whole list like node.next = null
    * or over expose the contents of rest of the linked list - This method was implemented as the helper method and to ease
    * the implementation process, and reduce the code quantity for each of the methods which depends on it. However, this can
    * lead to some serious security concerns in the software program if manipulated with correct tools.
    * */
    public ShowNode getAtIndex(int index) {
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

    /**
     * Inserts the show at particular index
     * @see TVShow
     * @param index represents the index in the tv list.
     * @param show represents the TVShow object.
     * */

    public void insertAtIndex(TVShow show, int index) throws NoSuchElementException {
        if (index < 0 || index > this.size) { throw new NoSuchElementException(); }
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

    /**
    * Check if the showID is present in the list.
    * @see ShowNode
    * @param id represents the showId of the showNode.
    * @return boolean - true if the show is contained in the list, false otherwise
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

    /**
     * Deletes the node from the index.
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

    /**
     * Deletes the node from the start of the linked lists.
     * @see ShowNode */

    public void deleteFromStart() {
        if (this.size == 0) { return; }
        this.head = this.head.next;
        this.size -= 1;
    }

    /**
     * Replaces the tv show node at the specific index of the linked list.
     * @param show - represent the content information of the new tv show object.
     * @param index - represents the index in the tv show list at which replacement needs to be performed.
     */

    public void replaceAtIndex(TVShow show, int index) {
        if (index >= this.size || index < 0) {
            System.out.println("Operation cannot be performed as " +
                    "invalid index is provided!");
            return;
        }
        ShowNode showNode = getAtIndex(index);
        showNode.show = show;
    }

}

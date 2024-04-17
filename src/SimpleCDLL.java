import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Simple doubly-linked lists.
 *
 * These do *not* (yet) support the Fail Fast policy.
 */
public class SimpleCDLL<T> implements SimpleList<T> {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The front of the list
   */
  Node2<T> front;

  /**
   * Dummy node. cannot be removed or accessed
   */
  private Node2<T> dummy;

  /**
   * The number of values in the list.
   */
  int size;

  /**
   * The number of changes made (to implement fail fast).
   */
  int numOfChanges = 0;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty list.
   */
  public SimpleCDLL() {
    this.front = this.dummy;
    this.size = 0;
    this.dummy = new Node2<T>(null);
    this.dummy.next = this.dummy;
    this.dummy.prev = this.dummy.next;
  } // SimpleDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      /**
       * The position in the list of the next value to be returned.
       * Included because ListIterators must provide nextIndex and
       * prevIndex.
       */
      int pos = 0;

      /**
       * The cursor is between neighboring values, so we start links
       * to the previous and next value..
       */
      Node2<T> prev = SimpleCDLL.this.dummy;
      Node2<T> next = SimpleCDLL.this.dummy.next;

      /**
       * The node to be updated by remove or set.  Has a value of
       * null when there is no such value.
       */
      Node2<T> update = null;

      /**
       * The number of changes made (to implement fail fast).
       */
      int numOfChanges = SimpleCDLL.this.numOfChanges;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      public void add(T val) throws UnsupportedOperationException {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if
        
        // Normal case
        this.prev = this.prev.insertAfter(val);

        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++SimpleCDLL.this.size;

        // Record change by iterator
        ++SimpleCDLL.this.numOfChanges;
        ++numOfChanges;

        // Update the position.  (See SimpleArrayList.java for more of
        // an explanation.)
        ++this.pos;
      } // add(T)

      public boolean hasNext() {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if

        return (this.next != SimpleCDLL.this.dummy);
      } // hasNext()

      public boolean hasPrevious() {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if
        return (this.prev != SimpleCDLL.this.dummy);
      } // hasPrevious()

      public T next() {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if

        if (!hasNext()) {
          throw new NoSuchElementException();
        } // if
        
        // Identify the node to update
        this.update = this.next;
        // Advance the cursor
        this.prev = this.next;
        this.next = this.next.next;
        // Note the movement
        ++this.pos;

        // And return the value
        return this.update.value;
      } // next()

      public int nextIndex() {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if
        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if
        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if
        if (!this.hasPrevious()) {
          throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.prev;
        // Advance the cursor
        this.next = this.prev;
        this.prev = this.prev.prev;
        // Note the movement
        --this.pos;

        // And return the value
        return this.update.value;
      } // previous()

      public void remove() {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if

        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Update the cursor
        if (this.next == this.update) {
          this.next = this.update.next;
        } // if
        if (this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos;
        } // if

        // Update the front
        if (SimpleCDLL.this.front == this.update) {
          SimpleCDLL.this.front = this.update.next;
        } // if

        // Do the real work
        this.update.remove();
        --SimpleCDLL.this.size;

        // Record change by iterator
        ++SimpleCDLL.this.numOfChanges;
        ++numOfChanges;

        // Note that no more updates are possible
        this.update = null;
      } // remove()

      public void set(T val) {
        // Check for recent changes
        if (this.numOfChanges != SimpleCDLL.this.numOfChanges) {
          throw new ConcurrentModificationException("Iterator is invalid!");
        } // if
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.value = val;
        // Note that no more updates are possible
        this.update = null;
      } // set(T)
    };
  } // listIterator()

} // class SimpleDLL<T>

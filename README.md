lab-linked-lists
================

### @author Arsal Shaikh
### @author Samuel A. Rebelsky

Code for a lab on implementing a simple list interface with doubly
linked lists.

### Use of the Dummy Node.  
The data structure has been implemented with the help of a dummy node. The dummy node helps simply the code especially by removing the need to check for special cases. For the `add()` and `remove()` methods, we can always assume the `next` and `prev` fields of the iterator will never be null as they will point to the dummy node. This is useful because we can use the `insertAfter()` method from the node class to add a node even in the empty case. Otherwise we would have needed to rewrite the code for initializing a new node and its pointers.  

#### GitHub Repo: https://github.com/shaikhar04/MP8-linked-lists-revisited.git
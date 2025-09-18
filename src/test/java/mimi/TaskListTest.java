package mimi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;


public class TaskListTest {

    @Test
    void markAndUnmark_updatesStatusIcon() {
        TaskList list = new TaskList(new ArrayList<>()); // pass an empty list
        Todo t = new Todo("read book");
        list.add(t);

        Task marked = list.mark(0);
        assertEquals("X", marked.getStatusIcon(), "should be marked");

        Task unmarked = list.unmark(0);
        assertEquals(" ", unmarked.getStatusIcon(), "should be unmarked");
    }

    @Test
    void remove_returnsTask_andShrinksList() {
        TaskList list = new TaskList(new ArrayList<>()); // pass an empty list
        list.add(new Todo("A"));
        list.add(new Todo("B"));

        Task removed = list.remove(0);
        assertEquals("A", removed.getDescription(), "removed task should be first");
        assertEquals(1, list.size(), "list size should shrink by one");
        assertEquals("B", list.get(0).getDescription(), "remaining first should be B");
    }

    @Test
    void remove_badIndex_throwsIndexOutOfBounds() {
        TaskList list = new TaskList(new ArrayList<>()); // pass an empty list
        list.add(new Todo("only one"));

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    void find_isCaseInsensitive_andIgnoresBlank() {
        TaskList list = new TaskList(new ArrayList<>());
        list.add(new Todo("Read Book"));
        list.add(new Todo("buy milk"));

        assertEquals(1, list.find("book").size());
        assertEquals(0, list.find("  ").size());
    }

}

package de.gruwie.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLinkedList<E> {

	private LinkedList<E> list;
	private Lock lock;
	
	public ConcurrentLinkedList () {
		this.list = new LinkedList<>();
		this.lock = new ReentrantLock();
	}
	
	/***
	 * see {@link java.util.concurrent.locks.Lock#lock()}
	 */
	public void lock() {
		lock.lock();
	}
	
	/***
	 * see {@link java.util.concurrent.locks.Lock#unlock()}
	 */
	public void unlock() {
		lock.unlock();
	}
	
	/**
	 * see {@link java.util.LinkedList#addFirst(Object)}
	 * @param item
	 */
	public void addFirst(E item) {
		lock.lock();
		list.addFirst(item);
		lock.unlock();
	}
	
	/***
	 * see {@link java.util.LinkedList#add(int, Object)}
	 * @param index
	 * @param item
	 */
	public void add(int index, E item) {
		lock.lock();
		list.add(index, item);
		lock.unlock();
	}
	
	/***
	 * see {@link java.util.List#add(Object)}
	 * @param item
	 */
	public void add(E item) {
		lock.lock();
		list.add(item);
		lock.unlock();
	}
	
	/***
	 * see {@link java.util.Collections#shuffle(List)}
	 */
	public void shuffle() {
		lock.lock();
		Collections.shuffle(list);
		lock.unlock();
	}
	
	/***
	 * @return {@link java.util.List#size()}
	 */
	public int size() {
		lock.lock();
		int size = list.size();
		lock.unlock();
		return size;
	}
	
	/***
	 * @param index
	 * @return {@link java.util.List#remove(int)}
	 */
	public E remove(int index) {
		lock.lock();
		E removed = list.remove(index);
		lock.unlock();
		return removed;
	}
	
	/***
	 * @param item
	 * @return {@link java.util.List#remove(Object)}
	 */
	public boolean remove(E item) {
		lock.lock();
		boolean success = list.remove(item);
		lock.unlock();
		return success;
	}
	
	/***
	 * @param index
	 * @return {@link java.util.List#get(int)}
	 */
	public E get(int index) {
		lock.lock();
		E result = list.get(index);
		lock.unlock();
		return result;
	}
	
	/***
	 * <b>NO</b> usage of locking
	 * @return <code>List</code> of all items stored inside the Thread-safe collection<br>Changes made to the returned collection will not change the orignal list<br>The copy is <b>NOT</b> thread-safe
	 */
	public List<E> getContentCopy() {
		List<E> copy = new LinkedList<>();
		list.forEach((k) -> copy.add(k));
		return copy;
	}
	
}

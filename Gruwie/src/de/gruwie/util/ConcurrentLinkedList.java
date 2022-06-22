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
	
	public void lock() {
		lock.lock();
	}
	
	public void unlock() {
		lock.unlock();
	}
	
	public void addFirst(E item) {
		lock.lock();
		list.addFirst(item);
		lock.unlock();
	}
	
	public void add(E item) {
		lock.lock();
		list.add(item);
		lock.unlock();
	}
	
	public void shuffle() {
		lock.lock();
		Collections.shuffle(list);
		lock.unlock();
	}
	
	public int size() {
		lock.lock();
		int size = list.size();
		lock.unlock();
		return size;
	}
	
	public E remove(int index) {
		lock.lock();
		E removed = list.remove(index);
		lock.unlock();
		return removed;
	}
	
	public boolean remove(E item) {
		lock.lock();
		boolean success = list.remove(item);
		lock.unlock();
		return success;
	}
	
	public E get(int index) {
		lock.lock();
		E result = list.get(index);
		lock.unlock();
		return result;
	}
	
	/***
	 * @return <code>List</code> of all items stored inside the Thread-safe collection<br>Changes made to the returned collection will not change the orignal list<br>The copy is <b>NOT</b> thread-safe
	 */
	public List<E> getContentCopy() {
		List<E> copy = new LinkedList<>();
		list.forEach((k) -> copy.add(k));
		return copy;
	}
	
}

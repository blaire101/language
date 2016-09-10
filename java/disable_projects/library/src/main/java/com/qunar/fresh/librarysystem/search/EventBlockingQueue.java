package com.qunar.fresh.librarysystem.search;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有事件支持的BlockingQueue
 * 
 * @author hang.gao
 * 
 */
class EventBlockingQueue<E> implements BlockingQueue<E> {

    /**
     * 被包装的BlockingQueue
     */
    private BlockingQueue<E> blockingQueue;

    /**
     * 用于发布事件的EventBus
     */
    private EventBus eventBus;

    private final Logger logger = LoggerFactory.getLogger(EventBlockingQueue.class);

    /**
     * 构造器
     * 
     * @param blockingQueue 被包装的对象
     * @param eventBus 使用的EventBus
     */
    public EventBlockingQueue(BlockingQueue<E> blockingQueue, EventBus eventBus) {
        super();
        Preconditions.checkArgument(blockingQueue != null && eventBus != null);
        this.eventBus = eventBus;
        this.blockingQueue = blockingQueue;
    }

    public int size() {
        return blockingQueue.size();
    }

    public boolean isEmpty() {
        return blockingQueue.isEmpty();
    }

    public boolean add(E e) {
        try {
            put(e);
        } catch (InterruptedException e1) {
            return false;
        }
        return true;
    }

    public Iterator<E> iterator() {
        return blockingQueue.iterator();
    }

    public E remove() {
        return blockingQueue.remove();
    }

    public boolean offer(E e) {
        return blockingQueue.add(e);
    }

    /**
     * 触发事件
     */
    private void invokeEvent() {
        if (remainingCapacity() == 0) {
            // 队列满了
            logger.trace("发布事件，更新索引");
            eventBus.post(this);
        }
    }

    public Object[] toArray() {
        return blockingQueue.toArray();
    }

    public E poll() {
        return blockingQueue.poll();
    }

    public E element() {
        return blockingQueue.element();
    }

    public E peek() {
        return blockingQueue.peek();
    }

    public <T> T[] toArray(T[] a) {
        return blockingQueue.toArray(a);
    }

    public void put(E e) throws InterruptedException {
        blockingQueue.put(e);
        invokeEvent();
    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return blockingQueue.add(e);
    }

    public E take() throws InterruptedException {
        return blockingQueue.take();
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return blockingQueue.poll(timeout, unit);
    }

    public int remainingCapacity() {
        return blockingQueue.remainingCapacity();
    }

    public boolean remove(Object o) {
        return blockingQueue.remove(o);
    }

    public boolean contains(Object o) {
        return blockingQueue.contains(o);
    }

    public int drainTo(Collection<? super E> c) {
        return blockingQueue.drainTo(c);
    }

    public boolean containsAll(Collection<?> c) {
        return blockingQueue.containsAll(c);
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        return blockingQueue.drainTo(c, maxElements);
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E e : c) {
            result = add(e);
        }
        return result;
    }

    public boolean removeAll(Collection<?> c) {
        return blockingQueue.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return blockingQueue.retainAll(c);
    }

    public void clear() {
        blockingQueue.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventBlockingQueue<?> other = (EventBlockingQueue<?>) obj;
        if (blockingQueue == null) {
            if (other.blockingQueue != null)
                return false;
        } else if (!blockingQueue.equals(other.blockingQueue))
            return false;
        if (eventBus == null) {
            if (other.eventBus != null)
                return false;
        } else if (!eventBus.equals(other.eventBus))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((blockingQueue == null) ? 0 : blockingQueue.hashCode());
        result = prime * result + ((eventBus == null) ? 0 : eventBus.hashCode());
        return result;
    }

}

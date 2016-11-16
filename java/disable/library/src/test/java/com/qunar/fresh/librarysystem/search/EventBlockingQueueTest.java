package com.qunar.fresh.librarysystem.search;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

import com.google.common.eventbus.EventBus;

/**
 * 
 * @author hang.gao
 * 
 */
public class EventBlockingQueueTest {

	/**
	 * 测试EventBlockingQueue，当put一个元素后，队列满，此时应该发布事件
	 * 
	 * @throws InterruptedException
	 * @author hang.gao
	 */
	@Test
	public void testPut_when_queue_is_full() throws InterruptedException {
		EventBus eventBus = mock(EventBus.class);
		EventBlockingQueue<Integer> blockingQueue = new EventBlockingQueue<Integer>(new ArrayBlockingQueue<Integer>(1), eventBus);
		blockingQueue.put(1);
		verify(eventBus).post(blockingQueue);
	}

	/**
	 * 测试EventBlockingQueue，当put一个元素后，队列满，此时应该发布事件
	 *
	 * @throws InterruptedException
	 * @author hang.gao
	 */
	@Test
	public void testPut_when_queue_is_not_full() throws InterruptedException {
		EventBus eventBus = mock(EventBus.class);
		EventBlockingQueue<Integer> blockingQueue = new EventBlockingQueue<Integer>(new ArrayBlockingQueue<Integer>(2), eventBus);
		blockingQueue.put(1);
		verify(eventBus, times(0)).post(blockingQueue);  //没有调用post方法
	}
}

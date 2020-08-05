package com.wsproject.batchservice.job.reader;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.batch.item.ItemReader;

/**
 * 데이터를 한 개씩 읽어오지 않고 복수의 데이터를 List형태로 한꺼번에 읽어온후, <br>
 * Queue에 저장하고 하나씩 읽어온다.
 * @author mslim
 *
 * @param <T>
 */
public class QueueItemReader<T> implements ItemReader<T> {
	
	private Queue<T> queue;
	
	public QueueItemReader(List<T> data) {
		this.queue = new LinkedList<T>(data);
	}

	@Override
	public T read() {
		return queue.poll();
	}
}

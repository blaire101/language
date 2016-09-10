package com.qunar.fresh.librarysystem.search.index;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.qunar.fresh.librarysystem.search.index.IndexUpdater.IndexExecutor;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * 
 * @author hang.gao
 *
 */
public class SimpleIndexUpdaterTest {

	@Test
	public void testUpdateIndex() throws IOException {
        IndexExecutor executor = mock(IndexExecutor.class);
		IndexUpdater updater = new SimpleIndexUpdater(new RAMDirectory());
		updater.updateIndex(executor, OpenMode.CREATE_OR_APPEND);
        verify(executor).onUpdate(Matchers.<IndexWriter>any());
	}
}

package com.qunar.fresh.librarysystem.dao;

/**
 * Created by hang.gao on 14-4-19.
 */
public class SimplePageFactory implements PageFactory {
    @Override
    public Page newPage(int offset, int limit) {
        return new SimplePage(offset, limit);
    }

    private static final class SimplePage implements Page {

        private int offset;

        private int limit;

        private SimplePage(int offset, int limit) {
            this.offset = offset;
            this.limit = limit;
        }

        @Override
        public int getOffset() {
            return offset;
        }

        @Override
        public int getLimit() {
            return limit;
        }
    }
}

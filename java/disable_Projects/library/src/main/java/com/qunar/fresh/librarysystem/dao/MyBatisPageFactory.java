package com.qunar.fresh.librarysystem.dao;

import org.apache.ibatis.session.RowBounds;

/**
 * 工厂，用于返回mybatis的Page对象
 * 
 * @author hang.gao
 */
public class MyBatisPageFactory implements PageFactory {
    @Override
    public Page newPage(int offset, int limit) {
        return new MyBatisPage(offset, limit);
    }

    /**
     * mybatis的Page实现，用作适配
     * 
     * @author hang.gao
     */
    class MyBatisPage extends RowBounds implements Page {
        public MyBatisPage() {
        }

        public MyBatisPage(int offset, int limit) {
            super(offset, limit);
        }

        @Override
        public int getOffset() {
            return super.getOffset();
        }

        @Override
        public int getLimit() {
            return super.getLimit();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            MyBatisPage that = (MyBatisPage) o;

            if (getLimit() != that.getLimit())
                return false;
            if (getOffset() != that.getOffset())
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = getOffset();
            result = 31 * result + getLimit();
            return result;
        }

        @Override
        public String toString() {
            return "Page[ offset:" + getOffset() + ",limit:" + getLimit() + "]";
        }
    }
}

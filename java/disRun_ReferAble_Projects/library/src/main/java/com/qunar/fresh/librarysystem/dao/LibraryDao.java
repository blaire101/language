package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.enums.LibraryStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-27 Time: 下午7:55
 * 
 * @author hang.gao
 * @author he.chen
 */
@Repository
public interface LibraryDao {

    public List<Library> fetchAllLibrarys();

    /**
     * 查询借阅期限
     * 
     * @return 借阅期限
     * @author hang.gao
     */
    Integer selectRedecoratePeriod(@Param("library") int libId);

    public Library fetchLibInfo(@Param("libId") Integer libId, @Param("libDept") String libDept,
            @Param("libName") String libName, @Param("status") LibraryStatus libraryStatus);

    public boolean addOneLibrary(@Param("libDept") String libDept, @Param("libName") String libName,
            @Param("status") LibraryStatus libraryStatus);

    public int libCount(@Param("libDept") String libDept, @Param("libName") String libName,
            @Param("status") LibraryStatus libraryStatus);

    public void deleteLibrary(@Param("libId") int libId);

}

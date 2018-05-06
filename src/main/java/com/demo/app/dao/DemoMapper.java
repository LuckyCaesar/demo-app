package com.demo.app.dao;

import com.demo.app.entity.DataEntity;
import com.demo.app.entity.DataQuery;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xiongcaesar on 2018/4/29.
 *
 */
@Repository
@Mapper
public interface DemoMapper {

    @Insert("<script>"
            + "INSERT INTO data_collection (`column_a`, `column_b`, `column_c`, `column_d`, `column_e`,"
            + "`column_f`, `column_g`, `column_h`, `column_i`, `column_j`, `column_k`, `column_l`, `quality`)"
            + " VALUES "
            + "<foreach collection =\"dataEntities\" item=\"data\" separator =\",\">"
            + "(#{data.columnA}, #{data.columnB}, #{data.columnC}, #{data.columnD}, #{data.columnE},"
            + " #{data.columnF}, #{data.columnG}, #{data.columnH}, #{data.columnI}, #{data.columnJ},"
            + " #{data.columnK}, #{data.columnL}, #{data.quality})"
            + "</foreach> "
            + "</script>")
    void insertList(@Param("dataEntities") List<DataEntity> dataEntities);

    @Select("SELECT `id`, `column_a` AS columnA, `column_b` AS columnB, `column_c` AS columnC, `column_d` AS columnD,"
            + "`column_e` AS columnE, `column_f` AS columnF, `column_g` AS columnG, `column_h` AS columnH,"
            + "`column_i` AS columnI, `column_j` AS columnJ, `column_k` AS columnK, `column_l` AS columnL,"
            + "`quality` FROM data_collection "
            + "LIMIT #{param.currentPage}, #{param.pageSize}")
    List<DataEntity> selectByParam(@Param("param")DataQuery query);

    @Select("SELECT COUNT(*) FROM data_collection")
    int countByParam(@Param("param")DataQuery query);

    @Select("SELECT COLUMN_NAME FROM information_schema.columns WHERE TABLE_NAME = 'data_collection'"
            + " AND COLUMN_NAME LIKE 'column%'")
    List<String> selectNeededColumns();

    @Select("SELECT AVG(${columnName}) FROM `data_collection`")
    BigDecimal selectAverageValue(@Param("columnName") String columnName);

    @Select("SELECT STDDEV_SAMP(${columnName}) FROM `data_collection`")
    BigDecimal selectStdValue(@Param("columnName") String columnName);

    @Select("SELECT count(*) FROM `data_collection` WHERE "
            + "${columnName} > "
            + "(SELECT AVG(${columnName}) + (STDDEV_SAMP(${columnName}) * 3) FROM `data_collection`)"
            + " OR "
            + "${columnName} < "
            + "(SELECT AVG(${columnName}) - (STDDEV_SAMP(${columnName}) * 3) FROM `data_collection`)")
    int selectOuliersNum(@Param("columnName") String columnName);

    @Select("SELECT CONCAT(quality, '=', count(*)) FROM `data_collection` GROUP BY quality")
    List<String> selectFactorInfo();
}

package com.demo.app;

import com.alibaba.fastjson.JSON;
import com.demo.app.biz.DemoBiz;
import com.demo.app.controller.DemoController;
import com.demo.app.dao.DemoMapper;
import com.demo.app.entity.DataEntity;
import com.demo.app.entity.DataQuery;
import com.demo.app.entity.common.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoAppApplicationTests {

	private MockMvc mockMvc;

//	private static  volatile AtomicInteger line = new AtomicInteger(1);
	private static int line = 1;

	@Resource
	private DemoBiz demoBiz;

	@Resource
	private DemoMapper demoMapper;

	@Resource
	private DemoController demoController;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(demoController).build();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testInsertDataList() {
		List<DataEntity> dataEntities = new ArrayList<>();
		DataEntity dataEntity = new DataEntity();
		dataEntity.setColumnA(new BigDecimal(1));
		dataEntity.setColumnB(new BigDecimal(1));
		dataEntity.setColumnC(new BigDecimal(1));
		dataEntity.setColumnD(new BigDecimal(1));
		dataEntity.setColumnE(new BigDecimal(1));
		dataEntity.setColumnF(new BigDecimal(1));
		dataEntity.setColumnG(new BigDecimal(1));
		dataEntity.setColumnH(new BigDecimal(1));
		dataEntity.setColumnI(new BigDecimal(1));
		dataEntity.setColumnJ(new BigDecimal(1));
		dataEntity.setColumnK(new BigDecimal(1));
		dataEntity.setColumnL(new BigDecimal(1));
		dataEntity.setQuality("bad");

		DataEntity dataEntity1 = new DataEntity();
		dataEntity1.setColumnA(new BigDecimal(1));
		dataEntity1.setColumnB(new BigDecimal(1));
		dataEntity1.setColumnC(new BigDecimal(1));
		dataEntity1.setColumnD(new BigDecimal(1));
		dataEntity1.setColumnE(new BigDecimal(1));
		dataEntity1.setColumnF(new BigDecimal(1));
		dataEntity1.setColumnG(new BigDecimal(1));
		dataEntity1.setColumnH(new BigDecimal(1));
		dataEntity1.setColumnI(new BigDecimal(1));
		dataEntity1.setColumnJ(new BigDecimal(1));
		dataEntity1.setColumnK(new BigDecimal(1));
		dataEntity1.setColumnL(new BigDecimal(1));
		dataEntity1.setQuality("ok");
		dataEntities.add(dataEntity);
		dataEntities.add(dataEntity1);
		demoMapper.insertList(dataEntities);
	}

	@Test
	public void testCountData() {
		System.out.println(demoBiz.countDataEntities(new DataQuery()));
	}

	@Test
	public void testGetDatas() throws Exception {
		DataQuery query = new DataQuery();
		query.setCurrentPage(2);
		query.setPageSize(20);
		List<DataEntity> dataEntities = demoBiz.getPagedDataEntities(query);
		System.out.println(JSON.toJSONString(dataEntities));
	}

	@Test
	public void editFile() {
		File getTargetFile = demoBiz.getTargetFile();
		List<String[]> contentArray = demoBiz.readFile(getTargetFile);
		Result result = demoBiz.editFile(contentArray, 7, 13, "ok_new");
		System.out.println(JSON.toJSONString(result));
	}

	@Test
	public void removeLineOrColumn() {
		File getTargetFile = demoBiz.getTargetFile();
		List<String[]> contentArray = demoBiz.readFile(getTargetFile);
		demoBiz.removeLine(contentArray, 6);
		System.out.println(JSON.toJSONString(demoBiz.writeFile(getTargetFile, contentArray)));

		Result<List<String[]>> removeColumnResult = demoBiz.removeColumn(contentArray, 6);
		System.out.println(JSON.toJSONString(demoBiz.writeFile(getTargetFile,
				removeColumnResult.getData())));
	}

	@Test
	public void multiThreadEditFile() {
		File targetFile = demoBiz.getTargetFile();
		List<String[]> contentArray = demoBiz.readFile(targetFile);
		CountDownLatch mainLatch = new CountDownLatch(1);
		CountDownLatch latch = new CountDownLatch(10);
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 10; i++) {
			Runnable runnable = () -> {
				try {
					mainLatch.await();
					synchronized ("editFile") {
						System.out.println("线程：" + line);
						demoBiz.editFile(contentArray, line, line, line + "_old_nsd");
						line++;
					}
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
			fixedThreadPool.execute(runnable);
		}
		try {
			mainLatch.countDown();
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(JSON.toJSONString(demoBiz.writeFile(targetFile, contentArray)));
		fixedThreadPool.shutdown();
	}

	@Test
	public void testDataAnalysis() {
//		System.out.println(demoMapper.selectNeededColumns());
//		System.out.println(demoMapper.selectAverageValue("column_a"));
//		System.out.println(demoMapper.selectStdValue("column_a"));
//		System.out.println(demoMapper.selectOuliersNum("column_a"));
//		System.out.println(demoMapper.selectFactorNum());
		System.out.println(JSON.toJSONString(demoBiz.getDataAnalysisResults()));
	}

}

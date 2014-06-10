/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * InputController.java
 * 2014-3-3
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ustc.sse.datamining.algo.ChineseTokenizer;
import ustc.sse.datamining.algo.DefaultStopWordsHandler;
import ustc.sse.datamining.algo.MultinomialModelNaiveBayes;
import ustc.sse.datamining.algo.TrainSampleDataManager;
import ustc.sse.datamining.algo.kmeans.KMean;
import ustc.sse.model.Classifer;
import ustc.sse.rjava.RJavaInterface;

/**
 * 实现功能： 数据挖掘控制器
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-3-3	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
@Controller
public class DataMiningController {
	private Log log = LogFactory.getLog(DataMiningController.class);
	public static HashMap<String, String> docClass = new HashMap<String,String>();
	//public static final String DEFAULT_DIR=System.getProperty("user.dir")+"../RDMP1/WEB-INF/classess/java/ustc/sse/datamining/algo/ik/";
	public static final String DEFAULT_DIR=TrainSampleDataManager.class.getResource("").getPath()+"/ik/";
	static{
		
	}
	
	public DataMiningController(){
		super();
	}
	
	/**
	 * 淘宝天池竞赛第一季 数据挖掘算法（数据挖掘+画图）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("dataMining")
	public String dataMining(HttpServletRequest request,HttpServletResponse response){
		if (!RJavaInterface.getRengine().waitForR()) {
        	log.error("Can not load R!");
        }
		
		//数据挖掘
		String cmd = "source('/home/starqiu/workspace/RDMP1/src/main/java/ustc/sse/r/tbmr.R',echo=TRUE)";
        String rv = "";
        try {
			rv = RJavaInterface.getRengine().eval(cmd).asString();
			log.info("data mining succeed!");
			log.info("the result of source tbmr.R is:"+rv);
		} catch (Exception e) {
			log.error("data mining failed!");
		}
        
        //画图
        cmd = "source('/home/starqiu/workspace/RDMP1/src/main/java/ustc/sse/r/draw.R',echo=TRUE)";
        try {
			rv = RJavaInterface.getRengine().eval(cmd).asString();
			log.info("data visual succeed!");
			log.info("the result of source draw.R is:"+rv);
		} catch (Exception e) {
			log.error("data visual failed!");
		}
        
        RJavaInterface.getRengine().end();
		return "taskDetail";
	}
	
	/**
	 * 文章自动分类算法输入页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("autoDocumentInput")
	public String autoDocumentInput(HttpServletRequest request,HttpServletResponse response){
		return "autoDocumentInput";
	}
	
	/**
	 * 文章自动分类算法
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("autoDocument")
	public String autoDocument(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		TrainSampleDataManager.process();
		String article=new String((request.getParameter("article")).getBytes("ISO-8859-1"),"UTF-8");
		//"据叙利亚国家电视台报道，针对西方国家即将发动的军事行动，叙利亚总统巴沙尔·阿萨德29日说，威胁发动敌对行动会让叙利亚更加坚持其原则和决定，如果遭受任何侵略，叙利亚将进行自卫。叙利亚安全部门官员29日称，该国军队已为最坏情况做好准备，称“将采取措施保卫国家，以及该以何种方式回应”。";
		//String s="两名要求匿名的消息人士称，两家公司的谈判已进入后期。另有一名消息人士称，Foursquare也在和其他公司协商投资事宜，而且这家公司未必与微软达成交易。";
		//String s="习近平来到沈阳机床集团。听说企业连续两年经营规模世界第一、职工75%以上是80后，总书记高兴地同“飞阳”团队年轻人攀谈起来";
		//String s="微软 盖茨称：作为接班计划委员会的一名成员，我将紧密与其他成员合作，从而挖掘出一名伟大的新任CEO。在新任CEO上任前，我们很幸运能够看到史蒂夫将继续行使其职责。";
		//String s="微软公司提出以446亿美元的价格收购雅虎中国网2月1日报道 美联社消息，微软公司提出以446亿美元现金加股票的价格收购搜索网站雅虎公司。微软提出以每股31美元的价格收购雅虎。微软的收购报价较雅虎1月31日的收盘价19.18美元溢价62%。微软公司称雅虎公司的股东可以选择以现金或股票进行交易。微软和雅虎公司在2006年底和2007年初已在寻求双方合作。而近两年，雅虎一直处于困境：市场份额下滑、运营业绩不佳、股价大幅下跌。对于力图在互联网市场有所作为的微软来说，收购雅虎无疑是一条捷径，因为双方具有非常强的互补性。(小桥)";
		//String s="谷歌之所以向Uber投资2.58亿美元，是为了从该公司的经验中吸取价值，而加盟董事会则是为了让Uber继续专注于无人驾驶出租车，而不要进军送货上门等其他领域。";
		//String s="谷歌，微软是很好的公司，百度和 淘宝也不错";
		Set<String> words=ChineseTokenizer.segStr(article).keySet();
		
		Map<String,BigDecimal> resultMap=MultinomialModelNaiveBayes.classifyResult(DefaultStopWordsHandler.dropStopWords(words));
		Set<String> set=resultMap.keySet();
		
		docClass.put("C000007", "汽车");
		docClass.put("C000008", "财经");
		docClass.put("C000010", "IT");
		docClass.put("C000013", "健康");
		docClass.put("C000014", "体育");
		docClass.put("C000016", "旅游");
		docClass.put("C000020", "教育");
		docClass.put("C000022", "招聘");
		docClass.put("C000023", "文化");
		docClass.put("C000024", "军事");
		
		List<Classifer> classifers = new LinkedList<Classifer>();
		BigDecimal total = new BigDecimal(0);
		BigDecimal probability = new BigDecimal(0);
		for(String str: set){
			probability = resultMap.get(str);
			total = total.add(probability);
			log.info("classifer:"+str+"     probability:"+ probability);
			Classifer classifer = new Classifer(docClass.get(str),probability);
			classifers.add(classifer);
		}
		//转化百分比
		for (Classifer classifer : classifers) {
			classifer.setProbability((classifer.getProbability()).divide(total,2, BigDecimal.ROUND_HALF_EVEN));
		}
		
		request.setAttribute("classifers", classifers);
		
		String classifyName = docClass.get(MultinomialModelNaiveBayes.getClassifyResultName());
		request.setAttribute("classifyName", classifyName);
		log.info("The final result:"+classifyName);
		return "autoDocument";
	}
	
	/**
	 * K-Means 聚类分析输入页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("kMeansInput")
	public String kMeansInput(HttpServletRequest request,HttpServletResponse response){
		return "kMeansInput";
	}
	
	/**
	 * K-Means 聚类分析
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("kMeans")
	public String kMeans(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String [] args = new String [4];
		args[0] = "-k";
		args[1] = request.getParameter("k");
		args[2] = "-n";
		args[3] = request.getParameter("numOfNode");
		KMean.kmeans(args);
		return "kMeans";
	}

}


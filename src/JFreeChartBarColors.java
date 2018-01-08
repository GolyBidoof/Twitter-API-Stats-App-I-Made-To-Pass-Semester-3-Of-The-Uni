import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import twitter4j.TwitterException;

import org.jfree.chart.ChartUtils;
public class JFreeChartBarColors {

	static File fontRegular = new File("src/RelevantFiles/Ubuntu.ttf");
	static File fontBold = new File("src/RelevantFiles/Ubuntu-Bold.ttf");
	static Font ubuntuFont;
	static Font ubuntuFontBold;
	static int chartNumber = 0;
	
	public static void initializeFont() throws FontFormatException, IOException {
		ubuntuFont = Font.createFont(Font.TRUETYPE_FONT, fontRegular);
		ubuntuFontBold = Font.createFont(Font.TRUETYPE_FONT, fontBold);
	}
	
	public static void saveAndPost(JFreeChart chart, String exportString) throws TwitterException, IOException {
		chartNumber++;
		String location = "data/chart" + chartNumber + ".png";
		OutputStream out = null;
		out = new FileOutputStream(new File(location));
		ChartUtils.writeChartAsPNG(out, chart, 1280, 720);
		File postableImage = new File(location);
		TwitterAPIPostingActions.writeATweetContainingAPic(exportString, postableImage);
	}
	
	//Tweet #1
	public static void languageUses(Map <String, Long> sortedMap) throws FontFormatException, IOException {
		initializeFont();
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Map.Entry <String, Long> entry: sortedMap.entrySet()) {
			String key = entry.getKey();
			Long val = entry.getValue();
			dataset.addValue((double) val, "Language uses", key);
		}

		JFreeChart chart = ChartFactory.createBarChart("Breakdown of the last 100 Tweets by language posted", "Language", "Amount of Tweets", dataset);

		CategoryPlot plot = chart.getCategoryPlot();
		chart.removeLegend();

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		ubuntuFont = ubuntuFont.deriveFont(20f);

		ValueAxis axis = plot.getRangeAxis();
		axis.setTickLabelFont(ubuntuFont);
		CategoryAxis axis2 = plot.getDomainAxis();
		axis2.setTickLabelFont(ubuntuFont);

		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		plot.getDomainAxis().setLabelFont(ubuntuFontBold);
		plot.getRangeAxis().setLabelFont(ubuntuFontBold);
		chart.getTitle().setFont(ubuntuFontBold);

		Color color = new Color(79, 129, 229);
		
		renderer.setSeriesPaint(0, color);
		renderer.setDrawBarOutline(true);
		renderer.setItemMargin(0);
		
		try {
			saveAndPost(chart, "In the last 100 tweets by " + AnalyzeLang.nickOfAPerson + ", the following languages were used.");
		} catch (TwitterException e1) {}
		
	}
	
	//Tweet #2
	public static void charactersInTweets(List<Integer> YouKnow) throws FontFormatException, IOException {
		initializeFont();
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int ID = 1;
		for (Integer entry: YouKnow) {
			dataset.addValue((double) entry, "Tweets in order", Integer.toString(ID));
			ID++;
		}

		JFreeChart chart = ChartFactory.createBarChart("Amount of characters represented in 100 last Tweets", "Tweet ID", "Character count", dataset);

		CategoryPlot plot = chart.getCategoryPlot();
		chart.removeLegend();

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		
		ubuntuFont = ubuntuFont.deriveFont(20f);
		ValueAxis axis = plot.getRangeAxis();
		axis.setTickLabelFont(ubuntuFont);
		CategoryAxis axis2 = plot.getDomainAxis();
		axis2.setTickLabelFont(ubuntuFont);
		axis2.setVisible(false);

		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		plot.getDomainAxis().setLabelFont(ubuntuFontBold);
		plot.getRangeAxis().setLabelFont(ubuntuFontBold);
		chart.getTitle().setFont(ubuntuFontBold);
		
		Color color = new Color(79, 189, 29);
		renderer.setSeriesPaint(0, color);
		renderer.setDrawBarOutline(true);
		renderer.setItemMargin(0);
		
		try {
			saveAndPost(chart, "Here's a breakdown of the last 100 Tweets character count by " + AnalyzeLang.nickOfAPerson + ".");
		} catch (TwitterException e1) {}
		
	}
	
	//Tweet #3
	public static void mentionPieChart(Map<String, Integer> sortedMentions) throws FontFormatException, IOException {
		initializeFont();
		
        DefaultPieDataset sortedMentionDataSet = new DefaultPieDataset();
        int count = 0, sum = 0;
        for (Map.Entry<String, Integer> entry : sortedMentions.entrySet())
        {
        	Integer value = entry.getValue();
        	if (count < 10) {
        		String key = entry.getKey();
        		sortedMentionDataSet.setValue(key, value);
        	} else {sum+=value;}
        	count++;
        }
        if (sum>0) sortedMentionDataSet.setValue("Others", sum);
                   
        JFreeChart mentions = ChartFactory.createPieChart("Most common mentions in the last 100 Tweets",sortedMentionDataSet,true,true,false);                
        PiePlot plot = (PiePlot) mentions.getPlot();
		
		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		ubuntuFont = ubuntuFont.deriveFont(22f);
		
        plot.setLabelFont(ubuntuFont);
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0.02);
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);    
        plot.setLabelBackgroundPaint(new Color(255, 255, 255));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
        
        mentions.getTitle().setFont(ubuntuFontBold);
        LegendTitle legend = mentions.getLegend();
        ubuntuFont = ubuntuFont.deriveFont(16f);
        legend.setFrame(BlockBorder.NONE);
        legend.setItemFont(ubuntuFont); 
        
        try {
			saveAndPost(mentions, "The most commonly mentioned people by " + AnalyzeLang.nickOfAPerson + " are depicted in the chart below.");
		} catch (TwitterException e1) {}
        
	}
	
}
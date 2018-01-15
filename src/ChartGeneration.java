import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import twitter4j.TwitterException;

public class ChartGeneration {

	static File fontRegular = new File("data/RelevantFiles/Ubuntu.ttf");
	static File fontBold = new File("data/RelevantFiles/Ubuntu-Bold.ttf");
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
		ChartUtils.writeChartAsPNG(out, chart, 1024, 576);
		File postableImage = new File(location);
		TwitterAPIPostingActions.writeATweetContainingAPic(exportString, postableImage);
	}

	// Tweet #1
	public static void languageUses(Map<String, Long> sortedMap, Integer totalSize)
			throws FontFormatException, IOException {
		initializeFont();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int size = 0;
		String mostCommon[] = new String[3];
		Long mostCommonVals[] = new Long[3];
		for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {
			String key = entry.getKey();
			Long val = entry.getValue();
			if (size < 3) {
				mostCommon[size] = key;
				mostCommonVals[size] = val;
			}
			dataset.addValue((double) val, "Language uses", key);
			size++;
		}

		JFreeChart chart = ChartFactory.createBarChart(
				"Breakdown of the last " + totalSize + " Tweets by " + MainClass.nickOfAPerson + " by language posted",
				"Language", "Amount of Tweets", dataset);

		CategoryPlot plot = chart.getCategoryPlot();
		chart.removeLegend();

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		ubuntuFont = ubuntuFont.deriveFont(16f);

		ValueAxis axis = plot.getRangeAxis();
		axis.setTickLabelFont(ubuntuFont);
		CategoryAxis axis2 = plot.getDomainAxis();
		axis2.setTickLabelFont(ubuntuFont);
		axis2.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		plot.getDomainAxis().setLabelFont(ubuntuFontBold);
		plot.getRangeAxis().setLabelFont(ubuntuFontBold);
		chart.getTitle().setFont(ubuntuFontBold);

		Color color = new Color(79, 129, 229);

		renderer.setSeriesPaint(0, color);
		renderer.setDrawBarOutline(true);
		renderer.setItemMargin(0);

		try {
			String output = "In the last " + totalSize + " Tweets by " + MainClass.nickOfAPerson
					+ ", the most popular language";
			int defaultSize = 3;
			if (sortedMap.size() < 3)
				defaultSize = sortedMap.size();
			if (defaultSize > 1)
				output += "s were ";
			else
				output += " was ";
			for (int i = 0; i < defaultSize; i++) {
				output += mostCommon[i] + " with " + mostCommonVals[i] + " Tweets";
				if (i == defaultSize - 3)
					output += ", ";
				else if (i == defaultSize - 2)
					output += " and ";
				else if (i == defaultSize - 1)
					output += ".";
			}
			saveAndPost(chart, output);
		} catch (TwitterException e1) {
		}

	}

	// Tweet #2
	public static void charactersInTweets(Integer[] totals, List<Integer[]> DividedIntoFourSections)
			throws FontFormatException, IOException {
		initializeFont();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int[][] data = new int[5][DividedIntoFourSections.size()];
		int ID = 0;
		for (Integer[] entry : DividedIntoFourSections) {
			for (int i = 0; i < 5; i++)
				data[i][ID] = entry[i];
			ID++;
		}
		int j = 0;
		if (ID>250) j=ID-250;
		for (int i=j; i < ID; i++) {
			dataset.addValue(data[0][i], "Capital letters", Integer.toString(i));
			dataset.addValue(data[1][i], "Lowercase letters", Integer.toString(i));
			dataset.addValue(data[2][i], "Numbers", Integer.toString(i));
			dataset.addValue(data[3][i], "Spaces", Integer.toString(i));
			dataset.addValue(data[4][i], "Other characters", Integer.toString(i));
		}
		String chartName = "Amount of characters represented in " + Integer.min(ID, 250) + " last Tweets by " + MainClass.nickOfAPerson;
		JFreeChart chart = ChartFactory.createStackedBarChart(chartName, "Tweet ID", "Character count", dataset);

		CategoryPlot plot = chart.getCategoryPlot();
		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		plot.setRenderer(renderer);

		ubuntuFont = ubuntuFont.deriveFont(20f);
		ValueAxis axis = plot.getRangeAxis();
		axis.setTickLabelFont(ubuntuFont);
		CategoryAxis axis2 = plot.getDomainAxis();
		axis2.setTickLabelFont(ubuntuFont);
		axis2.setVisible(false);

		LegendTitle legend = chart.getLegend();

		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		plot.getDomainAxis().setLabelFont(ubuntuFontBold);
		plot.getRangeAxis().setLabelFont(ubuntuFontBold);
		chart.getTitle().setFont(ubuntuFontBold);
		ubuntuFont = ubuntuFont.deriveFont(16f);
		legend.setItemFont(ubuntuFont);

		Color[] colors = new Color[5];
		colors[0] = new Color(127, 104, 221);
		colors[1] = new Color(104, 160, 221);
		colors[2] = new Color(188, 150, 88);
		colors[3] = new Color(229, 229, 229);
		colors[4] = new Color(130, 130, 130);
		for (int i = 0; i < 5; i++)
			renderer.setSeriesPaint(i, colors[i]);
		renderer.setDrawBarOutline(true);
		renderer.setItemMargin(0);
		try {
			saveAndPost(chart,
					MainClass.nickOfAPerson + " in his last " + ID + " Tweets wrote " + totals[0] + " capital letters, "
							+ totals[1] + " lowercase letters, " + totals[2] + " numbers, " + totals[3] + " spaces and "
							+ totals[4] + " other characters.");
		} catch (TwitterException e1) {
		}
		charactersInTweetsBreakdown(colors, totals);

	}

	// Tweet #3
	public static void charactersInTweetsBreakdown(Color[] colors, Integer[] totals)
			throws FontFormatException, IOException {

		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Capital letters", totals[0]);
		dataset.setValue("Lowercase letters", totals[1]);
		dataset.setValue("Numbers", totals[2]);
		dataset.setValue("Spaces", totals[3]);
		dataset.setValue("Other characters", totals[4]);

		int total = 0;
		double totalPercentages[] = new double[5];
		for (int i = 0; i < 5; i++)
			total += totals[i];
		for (int i = 0; i < 5; i++)
			totalPercentages[i] = (double) totals[i] * 100 / (double) total;

		JFreeChart mentions = ChartFactory.createPieChart(
				"Percentage breakdown of the total usage of " + Integer.toString(total) + " different signs in Tweets",
				dataset, true, true, false);
		PiePlot plot = (PiePlot) mentions.getPlot();

		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		ubuntuFont = ubuntuFont.deriveFont(22f);
		plot.setNoDataMessage("No data available");
		plot.setCircular(true);
		plot.setLabelFont(ubuntuFont);
		plot.setLabelGap(0.02);
		plot.setLabelOutlinePaint(null);
		plot.setLabelShadowPaint(null);
		plot.setLabelBackgroundPaint(new Color(255, 255, 255));
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} - {1} ({2})"));
		mentions.getTitle().setFont(ubuntuFontBold);

		for (int i = 0; i < dataset.getItemCount(); i++) {
			plot.setSectionPaint(dataset.getKey(i), colors[i]);
		}

		LegendTitle legend = mentions.getLegend();
		ubuntuFont = ubuntuFont.deriveFont(16f);
		legend.setFrame(BlockBorder.NONE);
		legend.setItemFont(ubuntuFont);

		try {
			saveAndPost(mentions,
					"In percents, " + String.format("%.2f", totalPercentages[0])
							+ "% was taken by uppercase characters, " + String.format("%.2f", totalPercentages[1])
							+ "% by lowercase characters, " + String.format("%.2f", totalPercentages[2])
							+ "% by numbers, " + String.format("%.2f", totalPercentages[3]) + "% by spaces and "
							+ String.format("%.2f", totalPercentages[4]) + "% by other characters.");
		} catch (TwitterException e1) {
		}

	}

	// Tweet #4
	public static void mentionPieChart(Map<String, Integer> sortedMentions) throws FontFormatException, IOException {
		initializeFont();

		DefaultPieDataset sortedMentionDataSet = new DefaultPieDataset();
		int count = 0, sum = 0;
		for (Map.Entry<String, Integer> entry : sortedMentions.entrySet()) {
			Integer value = entry.getValue();
			if (count < 10) {
				String key = entry.getKey();
				sortedMentionDataSet.setValue(key, value);
			} else {
				sum += value;
			}
			count++;
		}
		if (sum > 0)
			sortedMentionDataSet.setValue("Others", sum);

		JFreeChart mentions = ChartFactory.createPieChart("Most common mentions in the last Tweets",
				sortedMentionDataSet, true, true, false);
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

		plot.setAutoPopulateSectionPaint(false);
		Color[] colors = new Color[11];
		for (int i = 0; i < 11; i++)
			colors[i] = new Color((int) (96 - (96 * i * 0.07)), (int) (61 - (61 * i * 0.07)),
					(int) (255 - (255 * i * 0.07)));
		for (int i = 0; i < sortedMentionDataSet.getItemCount(); i++) {
			plot.setSectionPaint(sortedMentionDataSet.getKey(i), colors[i % 11]);
		}

		mentions.getTitle().setFont(ubuntuFontBold);
		LegendTitle legend = mentions.getLegend();
		ubuntuFont = ubuntuFont.deriveFont(16f);
		legend.setFrame(BlockBorder.NONE);
		legend.setItemFont(ubuntuFont);
		try {
			saveAndPost(mentions, "The most commonly mentioned people by " + MainClass.nickOfAPerson
					+ " are depicted in the chart below.");
		} catch (TwitterException e1) {
		}

	}

	// Tweet #5
	public static void datesLineChart(Map<String, Long> datesWeeksOccurences) throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		// Find lowest
		Integer lowest = 999999;
		Integer highest = Integer.parseInt(HandleTweetStats.currentWeek);
		for (Map.Entry<String, Long> entry : datesWeeksOccurences.entrySet()) {
			if (Integer.parseInt(entry.getKey()) < lowest)
				lowest = Integer.parseInt(entry.getKey());
		}
		Integer sum = 0, absoluteSum = 0;
		Integer idCountingDown = highest % 100;
		Integer yearCountingDown = highest / 100;
		Boolean thisHappened = false;
		String lowestValue = yearCountingDown.toString() + (idCountingDown > 9 ? idCountingDown.toString() : "0" + idCountingDown.toString());
		List<String> weeks = new ArrayList<String>();
		
		for (int i=0; i<30; i++) {
			if (thisHappened && i>4) break;
			lowestValue = yearCountingDown.toString() + (idCountingDown > 9 ? idCountingDown.toString() : "0" + idCountingDown.toString());
			dataset.addValue(0, "Amount of Tweets posted", lowestValue);
			idCountingDown--;
			if (idCountingDown < 1) {
				idCountingDown = 53;
				yearCountingDown--;
			}
			weeks.add(lowestValue);
			if(lowestValue.equals(lowest.toString())) thisHappened = true;
		}
		
		for (Map.Entry<String, Long> entry : datesWeeksOccurences.entrySet()) {
			String key = entry.getKey(); 
			Long val = entry.getValue();
			absoluteSum+=val.intValue();
			for(String s : weeks)
			    if(s.contains(key)) {
			    	dataset.setValue(val.intValue(), "Amount of Tweets posted", key);
			    	sum+=val.intValue();
			    }
		}

		final JFreeChart lineChart = ChartFactory.createLineChart(sum + " Tweets posted in last " + weeks.size() + " weeks", // chart title
				"Weeks and years", // domain axis label
				"Posted tweets", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
		);
		// XYPlot plot = lineChart.getXYPlot();

		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		ubuntuFont = ubuntuFont.deriveFont(22f);
		final CategoryPlot plot = lineChart.getCategoryPlot();
		plot.getRenderer().setSeriesStroke(0, new BasicStroke(5));

		ubuntuFont = ubuntuFont.deriveFont(16f);
		ValueAxis axis = plot.getRangeAxis();
		axis.setTickLabelFont(ubuntuFont);
		CategoryAxis axis2 = plot.getDomainAxis();
		axis2.setTickLabelFont(ubuntuFont);
		axis2.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
		//Int values
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());		

		LegendTitle legend = lineChart.getLegend();
		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		plot.getDomainAxis().setLabelFont(ubuntuFontBold);
		plot.getRangeAxis().setLabelFont(ubuntuFontBold);
		lineChart.getTitle().setFont(ubuntuFontBold);
		ubuntuFont = ubuntuFont.deriveFont(16f);
		legend.setItemFont(ubuntuFont);

		try {
			if (absoluteSum.equals(sum)) {
				saveAndPost(lineChart, "All " + absoluteSum + " Tweets were posted in the last " + weeks.size() + " weeks.");
			} else {
				saveAndPost(lineChart, "In the last 30 weeks, " + sum + " Tweets were posted.");
			}
		} catch (TwitterException e1) {
		}
	}

}
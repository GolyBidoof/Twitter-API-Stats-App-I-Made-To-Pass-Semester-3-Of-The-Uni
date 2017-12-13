import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import twitter4j.TwitterException;

import org.jfree.chart.ChartUtils;
public class JFreeChartBarColors {

	public static void color(Map < String, Long > sortedMap) throws FontFormatException,
	IOException {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Map.Entry < String, Long > entry: sortedMap.entrySet()) {
			String key = entry.getKey();
			Long val = entry.getValue();
			dataset.addValue((double) val, "Language uses", key);
		}

		/* create chart */
		JFreeChart chart = ChartFactory.createBarChart("Breakdown of the last 100 Tweets by Fyne-Q by language posted", "Language", "Amount of Tweets", dataset);

		/* Get instance of CategoryPlot */
		CategoryPlot plot = chart.getCategoryPlot();
		chart.removeLegend();

		/* Change Bar colors */
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		/* Change font */

		File f = new File("src/Ubuntu.ttf");
		File f1 = new File("src/Ubuntu-Bold.ttf");
		Font ubuntuFont = Font.createFont(Font.TRUETYPE_FONT, f);
		Font ubuntuFontBold = Font.createFont(Font.TRUETYPE_FONT, f1);
		ubuntuFont = ubuntuFont.deriveFont(20f);

		ValueAxis axis = plot.getRangeAxis();
		axis.setTickLabelFont(ubuntuFont);
		CategoryAxis axis2 = plot.getDomainAxis();
		axis2.setTickLabelFont(ubuntuFont);

		ubuntuFontBold = ubuntuFontBold.deriveFont(28f);
		plot.getDomainAxis().setLabelFont(ubuntuFontBold);
		plot.getRangeAxis().setLabelFont(ubuntuFontBold);
		chart.getTitle().setFont(ubuntuFontBold);

		// set the color (r,g,b) or (r,g,b,a)
		Color color = new Color(79, 129, 189);

		renderer.setSeriesPaint(0, color);

		//renderer.setSeriesPaint(0, Color.blue);
		renderer.setDrawBarOutline(true);
		renderer.setItemMargin(0);

		OutputStream out = null;

		try {
			out = new FileOutputStream(new File("data/chart.png"));
			ChartUtils.writeChartAsPNG(out, chart, 1280, 720);
		} catch(IOException e) {
			e.printStackTrace();
		}
		File postableImage = new File("data/chart.png");
		try {
			TwitterAPIPostingActions.writeATweetContainingAPic("Here's some stats for Fyne-Q", postableImage);
		} catch(TwitterException e) {
			e.printStackTrace();
		}
	}
}
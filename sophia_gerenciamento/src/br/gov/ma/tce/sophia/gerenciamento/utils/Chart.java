package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 * Classe utilizada para gerar o gráfico das médias da Avaliação de Reação 
 * @author eduardoroger
 *
 */
public class Chart {
	
	/**
	 * Gera um gráfico de barras e retorna a imagem do gráfico 
	 * 
	 * @param medias  as médias de todas as questões abordadas na Avaliação de Reação
	 * @return        a imagem do gráfico para ser colocada no relatório
	 */
	public static BufferedImage gerarChart(List<Double> medias) {
	
		DefaultCategoryDataset dataBar = new DefaultCategoryDataset();
		dataBar.setValue(medias.get(0), "CONTEÚDO DO CURSO", "");
		dataBar.setValue(medias.get(1), "TEMPO DE DURAÇÃO X CONTEÚDO", "");
		dataBar.setValue(medias.get(2), "METODOLOGIA", "");
		dataBar.setValue(medias.get(3), "DOMÍNIO DO INSTRUTOR", "");
		if (medias.get(4)!=0.0) {
			dataBar.setValue(medias.get(4), "MATERIAL DIDÁTICO", "");
		}
		dataBar.setValue(medias.get(5), "APOIO INSTITUCIONAL", "");
		dataBar.setValue(medias.get(6), "MÉDIA GERAL", "");	
		
		JFreeChart chart = ChartFactory.createBarChart3D("", "", "", dataBar, PlotOrientation.VERTICAL, 
				true, true, true);
		
		//Coloca como valor máximo 4 no gráfico
		chart.getCategoryPlot().getRangeAxis().setUpperBound(4.0);
		
		//Coloca a legenda à direita do gráfico
		LegendTitle legend = chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		
		//Coloca o valor da média no topo da barra
		BarRenderer3D renderer = (BarRenderer3D) chart.getCategoryPlot().getRenderer();
        renderer.setDrawBarOutline(false);
        for(int i = 0; i<medias.size();i++) {
        	renderer.setSeriesItemLabelGenerator(i, new StandardCategoryItemLabelGenerator());
        }
		renderer.setBaseItemLabelsVisible(true);	
		chart.getCategoryPlot().setRenderer(renderer);
		
		BufferedImage img = extrairImagem(chart, 800, 350);
		
		return img;	
	}
	
	/**
	 * Extrai imagem do gráfico
	 * 
	 * @param chart gráfico
	 * @param width largura da imagem
	 * @param height altura da imagem
	 * @return a imagem do gráfico
	 */
	private static BufferedImage extrairImagem(JFreeChart chart, int width, int height) {
        BufferedImage img =
                new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = img.createGraphics();
        chart.draw(g2, new Rectangle2D.Double(0, 0, width, height));
               
        g2.dispose();
        return img;

    }
}

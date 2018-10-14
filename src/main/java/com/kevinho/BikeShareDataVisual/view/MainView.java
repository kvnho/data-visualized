package com.kevinho.BikeShareDataVisual.view;


import com.kevinho.BikeShareDataVisual.entity.Station;
import com.kevinho.BikeShareDataVisual.service.DataService;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Route("")
@PageTitle("Metro Bike Shara Data Visualized")
public class MainView extends VerticalLayout {

    @Autowired
    private DataService dataService;


    private Board board;
    private Chart barChart;
    private Chart pieChart;
    private Chart lineChart;
    private Chart child3Chart;
    private Grid<Station> grid;
    private Grid<Station> grid2;
    private Div child;
    private Div child2;
    private Div child3;
    private Div child4;
    private Div child5;
    private Div child6;
    private VerticalLayout gridLayout;
    private VerticalLayout gridLayout2;

    private final String URL = "https://bikeshare.metro.net/stations/json/";
    private OkHttpClient client;
    private Response response;



    public MainView() throws IOException {
        getStyle().set("background", "#F7F7F7");
        setWidth("100%");
        initBoard();

        pieChart = routeCategoriesChart();
        child.add(pieChart);

        barChart = passholderChart();
        child2.add(barChart);

        child3Chart = passHolderAndTripRouteChart();
        child3.add(child3Chart);

        lineChart = ridersPerMonthChart();
        child4.add(lineChart);

        initGridLayout("Popular Starting Stations", "Popular Ending Stations");


        board.addRow(setHeader());
        board.addRow(child, child2);

        board.addRow(child3);

        board.addRow(child4);
        board.addRow(gridLayout, gridLayout2);


        add(board);
    }


    public Chart passHolderAndTripRouteChart(){
        HashMap<String, Integer> combos = DataService.passAndTripCombos();
        int flexPassOneWay = combos.get("Flex PassOne Way");
        int flexPassRoundTrip = combos.get("Flex PassRound Trip");
        int flexPassTotal = flexPassOneWay + flexPassRoundTrip;

        int monthlyPassOneWay = combos.get("Monthly PassOne Way");
        int monthlyPassRoundTrip = combos.get("Monthly PassRound Trip");
        int monthlyPassTotal = monthlyPassOneWay + monthlyPassRoundTrip;

        int walkupPassOneWay = combos.get("Walk-upOne Way");
        int walkupPassRoundTrip = combos.get("Walk-upRound Trip");
        int walkupPassTotal = walkupPassOneWay + walkupPassRoundTrip;


        Chart barChart = new Chart(ChartType.BAR);
        Configuration configuration = barChart.getConfiguration();
        configuration.setTitle("<b>Passholder Types and Trip Route Category<b>");

        barChart.setHeight("600px");

        Tooltip tooltip = new Tooltip();
        tooltip.setFollowPointer(true);
        configuration.setTooltip(tooltip);


        ListSeries totalSeries = new ListSeries("Total Trips", flexPassTotal, monthlyPassTotal, walkupPassTotal);
        ListSeries oneWaySeries = new ListSeries("One Way", flexPassOneWay, monthlyPassOneWay, walkupPassOneWay);
        ListSeries roundTripSeries = new ListSeries("Round Trip", flexPassRoundTrip, monthlyPassRoundTrip, walkupPassRoundTrip);

        configuration.addSeries(totalSeries);
        configuration.addSeries(oneWaySeries);
        configuration.addSeries(roundTripSeries);

        XAxis x = new XAxis();
        x.setCategories("Flex Pass", "Monthly Pass", "Walk-up");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Number of Riders");
        configuration.addyAxis(y);

        return barChart;
    }

    public Chart passholderChart(){
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        HashMap<String, Integer> passholdersMap = DataService.getPassholderType();

        float total = 0;
        for(int a : passholdersMap.values()){
            total += a;
        }
        double flexPass = Double.parseDouble(decimalFormat.format(passholdersMap.get("Flex Pass")*100 / total));
        double monthlyPass = Double.parseDouble(decimalFormat.format(passholdersMap.get("Monthly Pass") / total * 100));
        double walkupPass = Double.parseDouble(decimalFormat.format(passholdersMap.get("Walk-up") / total * 100));

        Chart pieChart = new Chart(ChartType.PIE);
        Configuration conf = pieChart.getConfiguration();

        conf.setTitle("<b>Passholder Types<b>");

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        tooltip.setPointFormat("<b>{point.percentage}%</b>");
        tooltip.setFollowPointer(true);
        conf.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Flex Pass", flexPass));
        series.add(new DataSeriesItem("Monthly Pass", monthlyPass));
        series.add(new DataSeriesItem("Walk-up", walkupPass));

        conf.setSeries(series);
        pieChart.setVisibilityTogglingDisabled(true);

        return pieChart;
    }

    public Chart routeCategoriesChart(){
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        int routes[] = DataService.routeCategories();
        double total = routes[0] + routes[1];
        double roundTrip = Double.parseDouble(decimalFormat.format((routes[0]*100) / total));
        double oneWay = Double.parseDouble(decimalFormat.format((routes[1]*100)/total));

        Chart pieChart = new Chart(ChartType.PIE);
        Configuration conf = pieChart.getConfiguration();

        conf.setTitle("<b>Route Categories<b>");

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(2);
        tooltip.setPointFormat("<b>{point.percentage}%</b>");
        conf.setTooltip(tooltip);
        tooltip.setFollowPointer(true);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);


        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("One Way", oneWay));
        series.add(new DataSeriesItem("Round Trip", roundTrip));

        conf.setSeries(series);
        pieChart.setVisibilityTogglingDisabled(true);


        return pieChart;
    }

    public Chart ridersPerMonthChart(){
        ArrayList<Map.Entry<Integer, Integer>> ridersPerMonthList = DataService.ridersPerMonth();

        Chart lineChart = new Chart(ChartType.SPLINE);
        Configuration configuration = lineChart.getConfiguration();
        configuration.setTitle("<b>Riders Per Month<b>");

        Tooltip tooltip = new Tooltip();
        tooltip.setFollowPointer(true);
        configuration.setTooltip(tooltip);

        XAxis x = new XAxis();
        x.setCategories("July 2017", "Aug 2017", "Sept 2017", "Oct 2017", "Nov 2017", "Dec 2017", "Jan 2018", "Feb 2018", "Mar 2018");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Number of Riders");
        configuration.addyAxis(y);

        DataSeries dataSeries = new DataSeries();
        String[] months = {"July 2017", "Aug 2017", "Sept 2017", "Oct 2017", "Nov 2017", "Dec 2017", "Jan 2018", "Feb 2018", "Mar 2018"};
        for(int i = 0; i < 6; i++){
            DataSeriesItem dataSeriesItem = new DataSeriesItem(months[i], ridersPerMonthList.get(i+3).getValue());
            dataSeries.add(dataSeriesItem);
        }
        for(int i = 6; i < 9; i++){
            DataSeriesItem dataSeriesItem = new DataSeriesItem(months[i], ridersPerMonthList.get(i-6).getValue());
            dataSeries.add(dataSeriesItem);
        }
        configuration.setSeries(dataSeries);
        configuration.getLegend().setEnabled(false);

        return lineChart;
    }

    public HorizontalLayout setHeader(){
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setSizeFull();
        headerLayout.setDefaultVerticalComponentAlignment(Alignment.AUTO);

        Label title = new Label("Metro Bike Share Data");
        title.getStyle().set("color", "black");
        title.getStyle().set("fontWeight", "bold");
        title.getStyle().set("fontSize", "100px");
        title.getStyle().set("fontFamily", "Arial");


        headerLayout.add(title);
        return headerLayout;
    }

    public void initBoard(){
        board = new Board();
        board.setWidth("99%");

        child = createDiv();
        child2 = createDiv();
        child3 = createDiv();
        child4 = createDiv();
        child5 = createDiv();
        child6 = createDiv();
    }

    private Div createDiv(){
        Div div = new Div();
        div.getStyle().set("margin", "75px 0px 75px 0px");
        div.getStyle().set("border", "1px solid lightgrey");
        div.getStyle().set("borderRadius", "10px");
        return div;
    }


    public Grid popularStations(ArrayList<Station> stationList, Grid initGrid) throws IOException {
        initGrid = new Grid<>(Station.class);

        initGrid.setItems(stationList);

        ListDataProvider<Station> listDataProvider = DataProvider.ofCollection(stationList);
        initGrid.setDataProvider(listDataProvider);

        initGrid.setColumns("id", "address", "visits", "bikes", "docks", "status");

        return initGrid;
    }

    public void initGridLayout(String gridName, String gridName2) throws IOException {
        Label label = new Label(gridName);
        Label label2 = new Label(gridName2);
        ArrayList<ArrayList<Station>> stations = getBothStationLists();

        gridLayout = new VerticalLayout();
        gridLayout2 = new VerticalLayout();

        grid = popularStations(stations.get(0), grid);
        grid2 = popularStations(stations.get(1), grid2);

        gridLayout.add(label, grid);
        gridLayout.setAlignItems(Alignment.CENTER);

        gridLayout2.add(label2, grid2);
        gridLayout2.setAlignItems(Alignment.CENTER);

    }

    @SuppressWarnings("Duplicates")
    public ArrayList<ArrayList<Station>> getBothStationLists() throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        response = client.newCall(request).execute();
        JSONArray jsonArray = new JSONObject(response.body().string()).getJSONArray("features");

        HashMap<Integer, Integer> popularStartStations = dataService.popularStartStations();
        HashMap<Integer, Integer> popularEndStations = dataService.popularEndStations();

        ArrayList<Station> popularStartStationsList = new ArrayList<>();
        ArrayList<Station> popularEndStationsList = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("properties");
            int key = Integer.parseInt(jsonObject.get("kioskId").toString());

            if(popularStartStations.containsKey(key)){
                Station station = new Station();
                station.setId(key);
                station.setVisits(popularStartStations.get(key));
                station.setAddress(jsonObject.getString("addressStreet"));
                station.setBikes(jsonObject.get("bikesAvailable").toString());
                station.setDocks(jsonObject.get("docksAvailable").toString());
                station.setStatus(jsonObject.getString("kioskPublicStatus"));

                popularStartStationsList.add(station);

            }
            if(popularEndStations.containsKey(key)){
                Station station = new Station();
                station.setId(key);
                station.setVisits(popularEndStations.get(key));
                station.setAddress(jsonObject.getString("addressStreet"));
                station.setBikes(jsonObject.get("bikesAvailable").toString());
                station.setDocks(jsonObject.get("docksAvailable").toString());
                station.setStatus(jsonObject.getString("kioskPublicStatus"));

                popularEndStationsList.add(station);
            }
        }

        ArrayList<ArrayList<Station>> stations = new ArrayList<>();

        stations.add(popularEndStationsList);
        stations.add(popularStartStationsList);

        return stations;
    }





}

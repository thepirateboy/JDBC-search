/*
    JDBC Search Application
    Basic search application to find customers by name, city, country and credit limit range
    ResultSet consists of customerNumber, customerName, city, country and creditLimit attributes
*/

package jdbc_search_test;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;


public class JDBC_Search_Test extends JFrame
{
    // CONTAINERS AND LAYOUTS
    private JFrame frame;
    private JPanel upperPanel;
    private JPanel lowerPanel;
    private FlowLayout flowLayout;
    private GridLayout gridLayout;
    // GUI COMPONENTS
    // labels
    private JLabel titleLabel;
    private JLabel customerNameLabel;
    private JLabel cityLabel;
    private JLabel countryLabel;
    private JLabel creditLimitLabel;
    private JLabel minCreditLimitLabel;
    private JLabel maxCreditLimitLabel;
    private JLabel resultsTitleLabel;
    private JLabel noticeLabel;
    // text fields
    private JTextField customerNameField;
    private JTextField cityField;
    private JTextField countryField;
    private JTextField minCreditLimitField;
    private JTextField maxCreditLimitField;
    private JTextField resultsFoundField;
    // buttons
    private JButton searchBtn;
    // table
    private JTable resultsTable;
    // table model
    private DefaultTableModel tableModel;
    // scroll pane
    private JScrollPane resultsTableScrollPane;


    // db_url for the database being connected to
    String db_url = "jdbc:mysql://localhost:3306/classicmodels";
    // database user name
    // replace <user_login> with your own login name
    String db_userName = "root";
    // database user password
    // replace <user_pwd> with your own password
    String db_userPwd = "Rafi2525";

    public JDBC_Search_Test()
    {
        // INITIALISE PRIVATE DATA MEMBERS
        // containers and layouts
        frame = new JFrame();
        upperPanel = new JPanel();
        lowerPanel = new JPanel();
        flowLayout = new FlowLayout();
        gridLayout = new GridLayout(2, 1);

        upperPanel.setBackground(Color.white);
        lowerPanel.setBackground(Color.white);

        // GUI components
        // labels
        titleLabel= new JLabel("Customer Search");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        titleLabel.setPreferredSize(new Dimension(750, 50));
        customerNameLabel = new JLabel("Name:");
        cityLabel= new JLabel("City:");
        countryLabel = new JLabel("Country:");
        creditLimitLabel= new JLabel("Credit Limit:");
        minCreditLimitLabel = new JLabel("MIN:");
        maxCreditLimitLabel = new JLabel("AND MAX:");
        resultsTitleLabel = new JLabel("Results");
        resultsTitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        resultsTitleLabel.setPreferredSize(new Dimension(550, 50));
        noticeLabel = new JLabel();
        noticeLabel.setPreferredSize(new Dimension(750, 50));

        // text fields
        customerNameField = new JTextField();
        customerNameField.setPreferredSize(new Dimension(200, 30));
        cityField = new JTextField();
        cityField.setPreferredSize(new Dimension(200, 30));
        countryField = new JTextField();
        countryField.setPreferredSize(new Dimension(200, 30));
        minCreditLimitField = new JTextField();
        maxCreditLimitField = new JTextField();
        minCreditLimitField.setPreferredSize(new Dimension(200, 30));
        maxCreditLimitField.setPreferredSize(new Dimension(200, 30));
        resultsFoundField = new JTextField();
        resultsFoundField.setPreferredSize(new Dimension (200, 30));
        resultsFoundField.setBorder(BorderFactory.createEmptyBorder());
        // button
        searchBtn = new JButton("Search");
        // table with columns (with headings) and default data
        String [] columnNames = new String[] {"ID", "Name", "City", "Country", "Credit Limit"};
        Object[] defaultData = {0, "Name", "City", "Country", 0};
        // set default table model
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        tableModel.addRow(defaultData);
        // set results table using tableModel with 5 columns and 1 row of default data
        resultsTable = new JTable(tableModel);
        // set preferred size of table
        resultsTable.setPreferredScrollableViewportSize(new Dimension(750, 150));
        // allow table to auto size all columns
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // auto resize can be turned off if needed
        // resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // limit the display of the table row data to the size of the scrollable viewport
        resultsTable.setFillsViewportHeight(true);
        // set up scroll pane with results table
        resultsTableScrollPane = new JScrollPane(resultsTable);
        // allow horizontal scrollbar as needed
        resultsTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // allow vertical scrollbar as needed
        resultsTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // add action listener to button (search database)
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String nameToSearch = "";
                String cityToSearch = "";
                String countryToSearch = "";
                int minCreditToSearch = 0;
                int maxCreditToSearch = 0;

                // get search field values

                // call SearchAndDisplayResults method
                SearchAndDisplayResults (nameToSearch, cityToSearch, countryToSearch, minCreditToSearch, maxCreditToSearch);
            }
        });

        // set grid layout for frame
        frame.setLayout(gridLayout);

        // add panels to frame
        frame.getContentPane().add(upperPanel);
        frame.getContentPane().add(lowerPanel);

        // set layouts to each panel
        upperPanel.setLayout(flowLayout);
        lowerPanel.setLayout(flowLayout);

        // add GUI components to upperPanel
        upperPanel.add(titleLabel);
        upperPanel.add(customerNameLabel);
        upperPanel.add(customerNameField);
        upperPanel.add(cityLabel);
        upperPanel.add(cityField);
        upperPanel.add(countryLabel);
        upperPanel.add(countryField);
        upperPanel.add(creditLimitLabel);
        upperPanel.add(minCreditLimitLabel);
        upperPanel.add(minCreditLimitField);
        upperPanel.add(maxCreditLimitLabel);
        upperPanel.add(maxCreditLimitField);
        upperPanel.add(searchBtn);

        // add GUI components to lowerPanel
        lowerPanel.add(resultsTitleLabel);
        lowerPanel.add(resultsFoundField);
        lowerPanel.add(resultsTableScrollPane);
        lowerPanel.add(noticeLabel);


        // set parameters for frame object (size, location on screen, window title, layout and visibility)
        frame.setSize(800, 600);
        frame.setLocation(0, 0);
        frame.setTitle("Customer Search");
        // auto-arrange the components inside the frame
        // frame.pack();
        frame.setVisible(true);
        // set up the normal close behaviour when the window close icon is clicked
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // displays results from sql query to the table GUI component using a 2D Object array
    // and the table model object
    public void setResultsToTable(Object[][] data)
    {
        // remove all existing rows
        if (tableModel.getRowCount() > 0)
        {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--)
            {
                tableModel.removeRow(i);
            }
        }

        if (data != null)
        {
            // add data to tableModel
            for (int row = 0; row < data.length; row++)
            {
                tableModel.addRow(data[row]);
            }
            // update table
            tableModel.fireTableDataChanged();
        }

    }

    // gets a database connection and executes a customised SQL query string
    // sets up the results in a 2D Object array and then calls the setResultsToTable() method
    public void SearchAndDisplayResults (String nameToSearch, String cityToSearch, String countryToSearch, int minCreditToSearch, int maxCreditToSearch)
    {
        String aa1 = "USA";


        try
        {
            String country1 = countryField.getText();
            String customerName1 = customerNameField.getText();
            String city1 = cityField.getText();
            String minCreditLimitField1 = minCreditLimitField.getText();
            String maxCreditLimitField1 = maxCreditLimitField.getText();
            // create connection
            Connection conn = DriverManager.getConnection(db_url, db_userName, db_userPwd);
            // create statement
            Statement stmt = conn.createStatement();
            // create SQL query
            String sql = "SELECT customerNumber, customerName, city, country, creditLimit FROM customers WHERE country LIKE '" + country1 + "%' AND ";
            sql += "customerName LIKE '" + customerName1 + "%' AND ";
            sql += "city LIKE '" + city1 + "%' AND ";
            sql += "creditLimit BETWEEN '" + minCreditLimitField1 + "' AND '" + maxCreditLimitField1 + "'";
            // modify SQL query depending on what search fields are used


            // display sql query string
            System.out.println(sql);
            // execute the query and store returned row results in a ResultSet object rs
            ResultSet rs = stmt.executeQuery(sql);
            // loop through each row stored in the result set object rs
            // and display


            if (rs != null)
            {
                // if different column names required
                // get names of columns and store in String[] columnNames
                /*
                ResultSetMetaData metaData = rs.getMetaData();
                String [] columnNames = new String [metaData.getColumnCount()];
                int columnCount = metaData.getColumnCount();
                for (int column = 1; column <= columnCount; column++)
                {
                    columnNames[column] = metaData.getColumnName(column);
                }
                */

                List<CustomerResult> resultList = new ArrayList<CustomerResult>();
                // get results data and store in 2D Object [][] dataSet object
                // required for resultsModel object to change table data
                Object[][] dataSet;
                // recordNbr
                int recordNbr = 0;

                while ( rs.next() )
                {
                    // get separate values for record set
                    int number = rs.getInt("customerNumber");
                    String name = rs.getString("customerName");
                    String city = rs.getString("city");
                    String country = rs.getString("country");
                    int creditLimit = rs.getInt("creditLimit");
                    // create CustomerResult object
                    CustomerResult cResult = new CustomerResult(number, name, city, country, creditLimit);
                    System.out.println(cResult.toString());
                    // add cResult to resultList
                    resultList.add(recordNbr, cResult);
                    // increment recordNbr
                    recordNbr++;
                }

                if (recordNbr > 0)
                {
                    dataSet = new Object [recordNbr][];
                    // add Object version of cResult object to dataSet
                    for (int i = 0; i < recordNbr; i++)
                    {
                        // add Object version of cResult object to dataSet
                        dataSet[i] = resultList.get(i).getObjectArray();
                    }

                    // set dataSet and display in resultsTable
                    setResultsToTable(dataSet);

                }
                // no result data
                else
                {
                    // removes existing data in table
                    setResultsToTable(null);
                }

                // display number of records found
                resultsFoundField.setText(recordNbr + " customer record(s) found!");

            }


            // close the connection
            conn.close();




        }
        // catch any SQL-related exceptions
        catch (SQLException e)
        {
            System.err.println("There's an SQL exception!");
            System.err.println(e.getMessage());
        }
        // catch any other exceptions
        catch (Exception e) {
            System.err.println("There's an exception!");
            System.err.println(e.getMessage());
        }

    }

    // main() method
    public static void main(String[] args)
    {
        // invoke instance of frame application
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JDBC_Search_Test searchTest = new JDBC_Search_Test();
            }
        });

    }
}
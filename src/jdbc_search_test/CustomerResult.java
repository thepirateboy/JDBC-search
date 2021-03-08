/*
    CustomerResult class
    stores each search result row of data
*/
package jdbc_search_test;


public class CustomerResult
{
    // private data fields
    private int customerNumber;
    private String customerName;
    private String city;
    private String country;
    private int creditLimit;

    // paramaterised constructor
    public CustomerResult(int customerNumber, String customerName, String city, String country, int creditLimit)
    {
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.city = city;
        this.country = country;
        this.creditLimit = creditLimit;
    }

    // no get() or set() methods used

    // used to return an Object [] array version of the CustomerResult instance
    // required to set up GUI table
    public Object [] getObjectArray()
    {
        Object [] obj = new Object[5];
        obj[0] = customerNumber;
        obj[1] = customerName;
        obj[2] = city;
        obj[3] = country;
        obj[4] = creditLimit;
        return obj;
    }

    // used for testing sql result
    @Override
    public String toString()
    {
        String csvStr = customerNumber + "," + customerName + "," + city + "," + country + "," + creditLimit;
        return csvStr;
    }
}

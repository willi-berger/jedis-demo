package com.alturos.adcup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 * test commit from eclipse
 * test commit with GitGui
 * test commit with GitGui from ws-2
 * merged to xytestName
 * eclipse xytestNameEclipse
 * mergerd with WS2 checkin xytestNameGITGUI
 * edited in eclipse 15:14
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String xytestNameGITGUI )
    {
        super( xytestNameGITGUI );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}

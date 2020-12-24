package fr.pantheonsorbonne.ufr27.miage.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestArretDAO.class, 
		TestHeureDePassageDAO.class, 
		TestInfoGareDAO.class, 
		TestPassagerDAO.class,
		TestTrainDAO.class })
public class AllTestsDAO {

}

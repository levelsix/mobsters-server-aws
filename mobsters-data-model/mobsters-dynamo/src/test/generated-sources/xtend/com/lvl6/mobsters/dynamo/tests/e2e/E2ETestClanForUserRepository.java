package com.lvl6.mobsters.dynamo.tests.e2e;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.lvl6.mobsters.dynamo.ClanForUser;
import com.lvl6.mobsters.dynamo.repository.ClanForUserRepository;
import com.lvl6.mobsters.dynamo.repository.DynamoRepositorySetup;
import com.lvl6.mobsters.dynamo.setup.SetupDynamoDB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-dynamo.xml")
@SuppressWarnings("all")
public class E2ETestClanForUserRepository {
  private final static Logger LOG = LoggerFactory.getLogger(E2ETestClanForUserRepository.class);
  
  @Extension
  private ListExtensions _listExtensions;
  
  @Autowired
  private SetupDynamoDB setup;
  
  @Autowired
  private DynamoDBMapper mapper;
  
  @Autowired
  private List<DynamoRepositorySetup> repoSetupList;
  
  @Autowired
  private ClanForUserRepository cfuRepo;
  
  private final static String userId = "0105ca66-59fc-4f24-8a83-63585e8c34ed";
  
  private final static String[] savedClanIds = { "07e2de0b-0c3a-4d6c-ab67-c098cf656d15", "48988898-e9ce-40f8-9f3e-20fdaedee14d", "858f8754-30a7-4c27-b010-5b56095b965f", "ff5a5449-ca20-480f-8729-ee97b5c0d9ab" };
  
  private final static String[] unsavedClanIds = { "09cc0113-3d8f-4264-a857-bd5cb78caead", "22c288b7-7456-437d-8431-b2e8cbccbcd1", "b8ba18fe-0221-43d6-b9ae-3030d6d1ff3f", "ae8a2497-b02d-48f7-b6a1-14c1a04a7453" };
  
  private final static Date requestTime = new Date();
  
  @Before
  public void createTestData() {
    E2ETestClanForUserRepository.LOG.info("BEGIN createTestData()");
    this.setup.checkDynamoTables();
    final Function1<String, ClanForUser> _function = new Function1<String, ClanForUser>() {
      public ClanForUser apply(final String clanId) {
        ClanForUser _clanForUser = new ClanForUser();
        final Procedure1<ClanForUser> _function = new Procedure1<ClanForUser>() {
          public void apply(final ClanForUser it) {
            it.setUserId(E2ETestClanForUserRepository.userId);
            it.setClanId(clanId);
            it.setStatus(clanId);
            it.setRequestTime(E2ETestClanForUserRepository.requestTime);
          }
        };
        return ObjectExtensions.<ClanForUser>operator_doubleArrow(_clanForUser, _function);
      }
    };
    List<ClanForUser> _map = ListExtensions.<String, ClanForUser>map(((List<String>)Conversions.doWrapArray(E2ETestClanForUserRepository.savedClanIds)), _function);
    final List<DynamoDBMapper.FailedBatch> failures = this.mapper.batchSave(_map);
    String _string = failures.toString();
    boolean _isEmpty = failures.isEmpty();
    Assert.assertTrue(_string, _isEmpty);
  }
  
  @After
  public void destroyTestData() {
    E2ETestClanForUserRepository.LOG.info("BEGIN destroyTestData()");
    for (final DynamoRepositorySetup repoSetup : this.repoSetupList) {
      repoSetup.dropTable();
    }
  }
  
  @Test
  public void testLoadEach() {
    try {
      E2ETestClanForUserRepository.LOG.info("BEGIN testLoadEach()");
      List<ClanForUser> _findByUserId = this.cfuRepo.findByUserId(E2ETestClanForUserRepository.userId);
      final List<ClanForUser> clans = new ArrayList<ClanForUser>(_findByUserId);
      int _size = clans.size();
      E2ETestClanForUserRepository.LOG.info("Found {} clans", Integer.valueOf(_size));
      final Function1<ClanForUser, String> _function = new Function1<ClanForUser, String>() {
        public String apply(final ClanForUser it) {
          return it.getClanId();
        }
      };
      List<String> _map = ListExtensions.<ClanForUser, String>map(clans, _function);
      String _string = _map.toString();
      E2ETestClanForUserRepository.LOG.info(_string);
      String _string_1 = ((List<String>)Conversions.doWrapArray(E2ETestClanForUserRepository.savedClanIds)).toString();
      E2ETestClanForUserRepository.LOG.info(_string_1);
      int _size_1 = clans.size();
      int _size_2 = ((List<String>)Conversions.doWrapArray(E2ETestClanForUserRepository.savedClanIds)).size();
      Assert.assertEquals("Found all clans", _size_1, _size_2);
      final Function1<ClanForUser, String> _function_1 = new Function1<ClanForUser, String>() {
        public String apply(final ClanForUser it) {
          return it.getClanId();
        }
      };
      List<String> _map_1 = ListExtensions.<ClanForUser, String>map(clans, _function_1);
      Assert.assertArrayEquals("Found all clans", E2ETestClanForUserRepository.savedClanIds, ((Object[])Conversions.unwrapArray(_map_1, Object.class)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void testLoadAll() {
    try {
      E2ETestClanForUserRepository.LOG.info("BEGIN testLoadAll()");
      List<ClanForUser> _loadAll = this.cfuRepo.loadAll(E2ETestClanForUserRepository.userId);
      final List<ClanForUser> clans = new ArrayList<ClanForUser>(_loadAll);
      int _size = clans.size();
      E2ETestClanForUserRepository.LOG.info("Found {} clans", Integer.valueOf(_size));
      int _size_1 = clans.size();
      int _size_2 = ((List<String>)Conversions.doWrapArray(E2ETestClanForUserRepository.savedClanIds)).size();
      Assert.assertEquals("Found all clans", _size_1, _size_2);
      final Function1<ClanForUser, String> _function = new Function1<ClanForUser, String>() {
        public String apply(final ClanForUser it) {
          return it.getClanId();
        }
      };
      List<String> _map = ListExtensions.<ClanForUser, String>map(clans, _function);
      Assert.assertArrayEquals("Found all clans", E2ETestClanForUserRepository.savedClanIds, ((Object[])Conversions.unwrapArray(_map, Object.class)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void testLoadLoop() {
    try {
      E2ETestClanForUserRepository.LOG.info("BEGIN testLoadLoop()");
      final Function1<String, ClanForUser> _function = new Function1<String, ClanForUser>() {
        public ClanForUser apply(final String it) {
          return E2ETestClanForUserRepository.this.cfuRepo.load(E2ETestClanForUserRepository.userId, it);
        }
      };
      final List<ClanForUser> clans = ListExtensions.<String, ClanForUser>map(((List<String>)Conversions.doWrapArray(E2ETestClanForUserRepository.savedClanIds)), _function);
      int _size = clans.size();
      E2ETestClanForUserRepository.LOG.info("Found {} clans", Integer.valueOf(_size));
      int _size_1 = ((List<String>)Conversions.doWrapArray(E2ETestClanForUserRepository.savedClanIds)).size();
      int _size_2 = clans.size();
      Assert.assertEquals("Found all clans", _size_1, _size_2);
      final Function1<ClanForUser, String> _function_1 = new Function1<ClanForUser, String>() {
        public String apply(final ClanForUser it) {
          return it.getClanId();
        }
      };
      List<String> _map = ListExtensions.<ClanForUser, String>map(clans, _function_1);
      Assert.assertArrayEquals("Found all clans", E2ETestClanForUserRepository.savedClanIds, ((Object[])Conversions.unwrapArray(_map, Object.class)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void setSetup(final SetupDynamoDB setup) {
    this.setup = setup;
  }
}

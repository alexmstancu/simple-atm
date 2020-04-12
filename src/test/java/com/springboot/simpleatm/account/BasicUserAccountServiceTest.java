package com.springboot.simpleatm.account;

import com.springboot.simpleatm.exception.UserAccountNotFoundException;
import com.springboot.simpleatm.model.UserAccount;
import com.springboot.simpleatm.repository.UserAccountRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BasicUserAccountServiceTest {
    private static final String ACCOUNT_NUMBER = "12345";
    private static final Double INITIAL_BALANCE = 2000D;


    @Injectable
    private UserAccountRepository userAccountRepository;

    @Tested
    private BasicUserAccountService userAccountService;

    @Test
    public void testFetchAccountDetails() {
        UserAccount dbUserAccount = createUserAccount(ACCOUNT_NUMBER, INITIAL_BALANCE);

        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = dbUserAccount;
        }};

        UserAccount serviceUserAccount = userAccountService.fetchAccountDetails(ACCOUNT_NUMBER);
        assertEquals(dbUserAccount, serviceUserAccount);
    }

    @Test(expected = UserAccountNotFoundException.class)
    public void givenAccountDoesNotExist_whenFetchAccountDetails_thenThrow() {
        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = null;
        }};

        userAccountService.fetchAccountDetails(ACCOUNT_NUMBER);
    }

    @Test
    public void testDeposit() {
        UserAccount dbUserAccount = createUserAccount(ACCOUNT_NUMBER, INITIAL_BALANCE);
        Double depositedAmount = 1000D;

        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = dbUserAccount;
        }};

        UserAccount serviceUserAccount = userAccountService.deposit(ACCOUNT_NUMBER, depositedAmount);

        new Verifications() {{
            UserAccount savedAccount;
            userAccountRepository.save(savedAccount = withCapture());
            times = 1;

            assertEquals(serviceUserAccount, savedAccount);
            assertEquals(dbUserAccount.getId(), savedAccount.getId());
            assertEquals(dbUserAccount.getName(), savedAccount.getName());
            assertEquals(dbUserAccount.getAccountNumber(), savedAccount.getAccountNumber());
            assertEquals(dbUserAccount.getPin(), savedAccount.getPin());
            Double expectedBalance = INITIAL_BALANCE + depositedAmount;
            assertEquals(expectedBalance, savedAccount.getBalance());

        }};
    }

    private UserAccount createUserAccount(String accountNumber, Double balance) {
        return UserAccount.builder()
                .accountNumber(accountNumber)
                .balance(balance)
                .name("dummy")
                .pin("1234")
                .id("someId")
                .build();
    }
}
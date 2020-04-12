package com.springboot.simpleatm.account;

import com.springboot.simpleatm.exception.InsufficientBalanceException;
import com.springboot.simpleatm.exception.InvalidAmountException;
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
    private static final Double INITIAL_BALANCE = 5000D;
    private static final Double VALID_AMOUNT = 1000D;
    private static final Double NEGATIVE_AMOUNT = -1000D;

    @Injectable
    private UserAccountRepository userAccountRepository;

    @Tested
    private BasicUserAccountService userAccountService;

    @Test
    public void testFetchAccountDetails() {
        UserAccount dbUserAccount = createUserAccount();

        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = dbUserAccount;
        }};

        UserAccount servicedUserAccount = userAccountService.fetchAccountDetails(ACCOUNT_NUMBER);
        assertEquals(dbUserAccount, servicedUserAccount);
    }

    @Test(expected = UserAccountNotFoundException.class)
    public void testFetchAccountDetailsWhenAccountNotFound() {
        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = null;
        }};

        userAccountService.fetchAccountDetails(ACCOUNT_NUMBER);
    }

    @Test
    public void testDeposit() {
        UserAccount dbUserAccount = createUserAccount();

        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = dbUserAccount;
        }};

        UserAccount servicedUserAccount = userAccountService.deposit(ACCOUNT_NUMBER, VALID_AMOUNT);

        new Verifications() {{
            UserAccount savedAccount;
            userAccountRepository.save(savedAccount = withCapture());
            times = 1;

            assertEquals(servicedUserAccount, savedAccount);
            assertEquals(dbUserAccount.getId(), savedAccount.getId());
            assertEquals(dbUserAccount.getName(), savedAccount.getName());
            assertEquals(dbUserAccount.getAccountNumber(), savedAccount.getAccountNumber());
            assertEquals(dbUserAccount.getPin(), savedAccount.getPin());
            Double expectedBalance = INITIAL_BALANCE + VALID_AMOUNT;
            assertEquals(expectedBalance, savedAccount.getBalance());

        }};
    }

    @Test(expected = UserAccountNotFoundException.class)
    public void testDepositWhenAccountNotFound() {
        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = null;
        }};

        userAccountService.deposit(ACCOUNT_NUMBER, VALID_AMOUNT);
    }

    @Test(expected = InvalidAmountException.class)
    public void testDepositWhenAmountIsZero() {
        userAccountService.deposit(ACCOUNT_NUMBER, 0D);
    }

    @Test(expected = InvalidAmountException.class)
    public void testDepositWhenAmountIsNegative() {
        userAccountService.deposit(ACCOUNT_NUMBER, NEGATIVE_AMOUNT);
    }

    @Test(expected = InvalidAmountException.class)
    public void testDepositWhenAmountIsNull() {
        userAccountService.deposit(ACCOUNT_NUMBER, null);
    }

    @Test
    public void testWithdraw() {
        UserAccount dbUserAccount = createUserAccount();

        new Expectations() {{
            userAccountRepository.findByAccountNumber(ACCOUNT_NUMBER);
            result = dbUserAccount;
        }};

        UserAccount servicedUserAccount = userAccountService.withdraw(ACCOUNT_NUMBER, VALID_AMOUNT);

        new Verifications() {{
            UserAccount savedAccount;
            userAccountRepository.save(savedAccount = withCapture());
            times = 1;

            assertEquals(servicedUserAccount, savedAccount);
            assertEquals(dbUserAccount.getId(), savedAccount.getId());
            assertEquals(dbUserAccount.getName(), savedAccount.getName());
            assertEquals(dbUserAccount.getAccountNumber(), savedAccount.getAccountNumber());
            assertEquals(dbUserAccount.getPin(), savedAccount.getPin());
            Double expectedBalance = INITIAL_BALANCE - VALID_AMOUNT;
            assertEquals(expectedBalance, savedAccount.getBalance());

        }};
    }

    @Test(expected = InvalidAmountException.class)
    public void testWithdrawWhenAmountIsZero() {
        userAccountService.withdraw(ACCOUNT_NUMBER, 0D);
    }

    @Test(expected = InvalidAmountException.class)
    public void testWithdrawWhenAmountIsNegative() {
        userAccountService.withdraw(ACCOUNT_NUMBER, NEGATIVE_AMOUNT);
    }

    @Test(expected = InvalidAmountException.class)
    public void testWithdrawWhenAmountIsNull() {
        userAccountService.withdraw(ACCOUNT_NUMBER, null);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void testWithdrawWhenInsufficientBalance() {
        Double withdrawAmount = INITIAL_BALANCE + 1D;
        userAccountService.withdraw(ACCOUNT_NUMBER, withdrawAmount);
    }

    private UserAccount createUserAccount() {
        return UserAccount.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .balance(INITIAL_BALANCE)
                .name("dummy")
                .pin("1234")
                .id("someId")
                .build();
    }
}
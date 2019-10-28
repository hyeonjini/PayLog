package com.kumoh.paylog2.dto;

/*
* 메인 Account, 리스트 Account 의 데이터를 나타내기 위한 DTO
* 그룹이름, 예산, 지출, 수입을 보여준다.*/
public class AccountInfo {
    private int accountId;
    private String name;
    private boolean isMain;
    private int budget;
    private int spending;
    private int income;

    public AccountInfo(int accountId, String name,boolean isMain, int budget, int spending, int income) {
        this.accountId = accountId;
        this.name = name;
        this.isMain = isMain;
        this.budget = budget;
        this.spending = spending;
        this.income = income;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getSpending() {
        return spending;
    }

    public void setSpending(int spending) {
        this.spending = spending;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }
}

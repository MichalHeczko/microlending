angular.module('ngBoilerplate.loan', ['ui.router', 'ngResource', 'ngBoilerplate.account', 'hateoas'])
.config(function($stateProvider) {
    $stateProvider.state('manageLoans', {
            url:'/manage/loans?accountId',
            views: {
                'main': {
                    templateUrl:'loan/manage-loans.tpl.html',
                    controller: 'ManageLoanCtrl'
                }
            },
            resolve: {
                account: function(accountService, $stateParams) {
                    return accountService.getAccountById($stateParams.accountId);
                },
                loans: function(loanService, $stateParams) {
                    return loanService.getLoansForAccount($stateParams.accountId);
                }
            },
            data : { pageTitle : "Loans" }
    });
})
.factory('loanService', function($resource, $q) {
      var service = {};
      service.createLoan = function(accountId, loanData) {
        var Loan = $resource("/microlending/rest/accounts/:paramAccountId/loans");
        return Loan.save({paramAccountId: accountId}, loanData).$promise;
      };
      service.getAllLoans = function() {
        var Loan = $resource("/microlending/rest/loans");
        return Loan.get().$promise.then(function(data) {
            return data.loans;
        });
      };
      service.getLoansForAccount = function(accountId) {
          var deferred = $q.defer();
          var Account = $resource("/microlending/rest/accounts/:paramAccountId");
          Account.get({paramAccountId:accountId}, function(account) {
              var Loan = account.resource('loans');
              Loan.get(null,
                  function(data) {
                    deferred.resolve(data.loans);
                  },
                  function() {
                    deferred.reject();
                  }
              );
          });
          return deferred.promise;
      };
      return service;
})
.controller("ManageLoanCtrl", function($scope, loans, account, loanService, $state) {
    $scope.name = account.name;
    $scope.loans = loans;
    $scope.createLoan = function(loanName) {
        loanService.createLoan(account.rid, {
            title : loanName
        }).then(function() {
            $state.go("manageLoans", { accountId : account.rid }, { reload : true });
        });
    };
});
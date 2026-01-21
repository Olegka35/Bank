package contracts.account

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'Update account balance'
    name 'update_account_balance'

    request {
        method POST()
        url '/account/oleg/cash'
        headers {
            contentType(applicationJson())
            header 'Authorization', value(
                    consumer(regex('Bearer\\s+.+')),
                    producer('Bearer test-token')
            )
        }
        body(
                value: 500,
                action: 'PUT'
        )
    }

    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
                balance: 1000
        )
    }
}
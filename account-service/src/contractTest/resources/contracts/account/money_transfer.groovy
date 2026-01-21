package contracts.account

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'Transfer money'
    name 'money_transfer'

    request {
        method POST()
        url '/account/oleg/transfer'
        headers {
            contentType(applicationJson())
            header 'Authorization', value(
                    consumer(regex('Bearer\\s+.+')),
                    producer('Bearer test-token')
            )
        }
        body(
                value: 12000,
                login: 'polukhina'
        )
    }

    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
                balance: 50000000
        )
    }
}
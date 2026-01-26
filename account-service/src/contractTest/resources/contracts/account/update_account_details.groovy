package contracts.account

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'Update account details'
    name 'update_account_details'

    request {
        method POST()
        url '/account'
        headers {
            contentType(applicationJson())
            header 'Authorization', value(
                    consumer(regex('Bearer\\s+.+')),
                    producer('Bearer test-token')
            )
        }
        body(
                name: "Oleg Tarasov",
                birthdate: '1997-03-23'
        )
    }

    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
                login: "oleg",
                fullName: "Oleg Tarasov",
                birthdate: '1997-03-23'
        )
    }
}
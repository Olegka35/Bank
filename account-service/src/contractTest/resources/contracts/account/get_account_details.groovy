package contracts.account

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'Get account details'
    name 'get_account_details'

    request {
        method GET()
        url '/account'
        headers {
            contentType(applicationJson())
            header 'Authorization', value(
                    consumer(regex('Bearer\\s+.+')),
                    producer('Bearer test-token')
            )
        }
    }

    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
                name: "Oleg Tarasov",
                birthdate: '1997-03-23',
                sum: 500_000_000,
                accounts: [
                        [
                                login: "ivanov",
                                name: "Pavel Ivanov"
                        ],
                        [
                                login: "bannova",
                                name: "Nastya Bannova"
                        ]
                ]
        )
    }
}
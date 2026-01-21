package contracts.notification

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'Send notification'
    name 'send_notification'

    request {
        method POST()
        url '/notify'
        headers {
            contentType(applicationJson())
            header 'Authorization', value(
                    consumer(regex('Bearer\\s+.+')),
                    producer('Bearer test-token')
            )
        }
        body(
                login: 'Oleg',
                message: 'Test notification'
        )
    }

    response {
        status OK()
    }
}
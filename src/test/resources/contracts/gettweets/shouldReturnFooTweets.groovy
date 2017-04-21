package contracts.gettweets

import org.springframework.cloud.contract.spec.Contract;

Contract.make {
    request {
        method 'GET'
        url '/v1/tweets'
        headers {
            header('Authorization', 'Basic Zm9vOnBhc3N3b3Jk')
        }
    }

    response {
        status 200
        body([
                [
                        tweetId  : '00000000-0000-0000-0000-000000000002',
                        text     : 'tweet3',
                        username : 'foo',
                        createdAt: $(client('2017-04-11T00:00:00Z'), server(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z')))
                ]
        ])
        headers {
            contentType('application/json')
        }
    }
}

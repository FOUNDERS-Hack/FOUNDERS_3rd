import React from 'react'
import { Button, Card, Image, Feed, Segment } from 'semantic-ui-react'

const CardAlert = () => (
  <Card.Group>

    <Card>
      <Card.Content>
        <Card.Header>미스트롯 최종 경연 투표</Card.Header>
        </Card.Content>
        <Card.Content>
          <Image
            size='large'
            src='/images/avatar/large/jenny.jpg'
          />
        </Card.Content>
        <Card.Content>
          <Feed>
            <Feed.Event>
              <Feed.Label image='/images/avatar/small/jenny.jpg' />
              <Feed.Content>
                <Feed.Summary>
                  <a>송가인</a>에게 투표하셨습니다.
                </Feed.Summary>
              </Feed.Content>
            </Feed.Event>
          </Feed>
      </Card.Content>
      <Card.Content>
        <Card.Description>
          당신의 한표가 블록체인에 영원히 기록되었습니다!
        </Card.Description>
      </Card.Content>
      <Card.Content extra>
        <div className='ui two buttons'>
          <Button basic color='green'>
            보러가기
          </Button>
          <Button basic color='red'>
            닫기
          </Button>
        </div>
      </Card.Content>
    </Card>
  </Card.Group>
)

export default CardAlert

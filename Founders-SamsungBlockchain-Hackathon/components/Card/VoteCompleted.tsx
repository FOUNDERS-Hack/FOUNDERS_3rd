import React from "react";
import { Button, Card, Image, Feed, Segment } from "semantic-ui-react";
import { STORE } from "../../constants/stores";
import { GlobalStore } from "../../stores/globalStore";
import { observer, inject } from "mobx-react";
import { voteAPI } from "../../API/VoteAPI";
import Link from "next/link";

interface IVoteCompletedProps {
  globalStore?: GlobalStore;
}

@inject(STORE.globalStore)
@observer
export default class VoteCompleted extends React.Component<IVoteCompletedProps> {
  componentDidMount = async () => {
    // await voteAPI.txGetMyVoteResult();
  };
  render() {
    return (
      <Card.Group>
        <Card>
          <Card.Content>
            <Card.Header>{this.props.globalStore.votedData.votedElectionName}</Card.Header>
          </Card.Content>
          <Card.Content>
            <Image size="large" src="https://react.semantic-ui.com/images/avatar/large/matthew.png" />
          </Card.Content>
          <Card.Content>
            <Feed>
              <Feed.Event>
                <Feed.Content>
                  <Feed.Summary style={{ textAlign: "center" }}>
                    <a>{this.props.globalStore.votedData.votedCandidateName}</a>에게 투표하셨습니다.
                  </Feed.Summary>
                </Feed.Content>
              </Feed.Event>
            </Feed>
          </Card.Content>
          <Card.Content>
            <Card.Description>당신의 한표가 블록체인에 영원히 기록되었습니다!</Card.Description>
          </Card.Content>
          <Card.Content extra>
            <div className="ui two buttons">
              <Button basic color="green">
                증서 발급받기
              </Button>
              <Link href="/index">
                <Button basic color="red">
                  닫기
                </Button>
              </Link>
            </div>
          </Card.Content>
        </Card>
      </Card.Group>
    );
  }
}

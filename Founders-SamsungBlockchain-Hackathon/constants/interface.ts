export interface IAnimalData {
  animalID?: string; // 동물 등록 번호
  name?: string; // 이름
  animalType?: string; // 품종
  color?: string; // 털색
  gender?: number; // 0 암 1 수 2 중성
  birth?: string; // 생년월일
  adoptionDate?: string; // 취득일
  remarks?: string; // 특이사항
}

export interface ICandidateData {
  candidateID?: number;
  candidateName?: string;
  voteCount?: number;
}
export interface IElectionData {
  electionID?: number; // 투표함 번호
  electionName?: string; // 투표함 이름
  voted?: boolean;
  totalVoted?: number;
}

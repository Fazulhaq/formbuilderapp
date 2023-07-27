import { IFeedbackhistory } from 'app/shared/model/feedbackhistory.model';

export interface IFilledForm {
  id?: number;
  jSONText?: string;
  feedbackhistories?: IFeedbackhistory[] | null;
}

export const defaultValue: Readonly<IFilledForm> = {};

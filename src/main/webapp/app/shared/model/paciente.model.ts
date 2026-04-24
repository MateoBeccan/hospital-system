import { type IFactorRh } from '@/shared/model/factor-rh.model';
import { type IGrupoSanguineo } from '@/shared/model/grupo-sanguineo.model';
import { type IObraSocial } from '@/shared/model/obra-social.model';
import { type IPersona } from '@/shared/model/persona.model';

export interface IPaciente {
  id?: number;
  numeroHistoriaClinica?: string;
  alergiasGenerales?: string | null;
  observaciones?: string | null;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  activo?: boolean;
  persona?: IPersona;
  obraSocial?: IObraSocial | null;
  grupoSanguineo?: IGrupoSanguineo | null;
  factorRh?: IFactorRh | null;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public numeroHistoriaClinica?: string,
    public alergiasGenerales?: string | null,
    public observaciones?: string | null,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public activo?: boolean,
    public persona?: IPersona,
    public obraSocial?: IObraSocial | null,
    public grupoSanguineo?: IGrupoSanguineo | null,
    public factorRh?: IFactorRh | null,
  ) {
    this.activo = this.activo ?? false;
  }
}

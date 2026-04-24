import { type IPaciente } from '@/shared/model/paciente.model';

export interface IHistoriaClinica {
  id?: number;
  numero?: string;
  fechaApertura?: Date;
  fechaUltimaActualizacion?: Date | null;
  antecedentesPersonales?: string | null;
  antecedentesFamiliares?: string | null;
  enfermedadesPrevias?: string | null;
  cirugiasPrevias?: string | null;
  alergias?: string | null;
  medicacionHabitual?: string | null;
  habitos?: string | null;
  observacionesGenerales?: string | null;
  activa?: boolean;
  fechaCierre?: Date | null;
  motivoCierre?: string | null;
  paciente?: IPaciente;
}

export class HistoriaClinica implements IHistoriaClinica {
  constructor(
    public id?: number,
    public numero?: string,
    public fechaApertura?: Date,
    public fechaUltimaActualizacion?: Date | null,
    public antecedentesPersonales?: string | null,
    public antecedentesFamiliares?: string | null,
    public enfermedadesPrevias?: string | null,
    public cirugiasPrevias?: string | null,
    public alergias?: string | null,
    public medicacionHabitual?: string | null,
    public habitos?: string | null,
    public observacionesGenerales?: string | null,
    public activa?: boolean,
    public fechaCierre?: Date | null,
    public motivoCierre?: string | null,
    public paciente?: IPaciente,
  ) {
    this.activa = this.activa ?? false;
  }
}

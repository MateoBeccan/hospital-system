import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Paciente } from '@/shared/model/paciente.model';

import PacienteService from './paciente.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Paciente Service', () => {
    let service: PacienteService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PacienteService();
      currentDate = new Date();
      elemDefault = new Paciente(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Paciente', async () => {
        const returnedFromService = {
          id: 123,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Paciente', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Paciente', async () => {
        const returnedFromService = {
          numeroHistoriaClinica: 'BBBBBB',
          alergiasGenerales: 'BBBBBB',
          observaciones: 'BBBBBB',
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          activo: true,
          ...elemDefault,
        };

        const expected = { fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Paciente', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Paciente', async () => {
        const patchObject = { observaciones: 'BBBBBB', fechaAlta: dayjs(currentDate).format(DATE_FORMAT), activo: true, ...new Paciente() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Paciente', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Paciente', async () => {
        const returnedFromService = {
          numeroHistoriaClinica: 'BBBBBB',
          alergiasGenerales: 'BBBBBB',
          observaciones: 'BBBBBB',
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          activo: true,
          ...elemDefault,
        };
        const expected = { fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Paciente', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Paciente', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Paciente', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
